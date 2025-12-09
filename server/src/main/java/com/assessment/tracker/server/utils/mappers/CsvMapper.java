package com.assessment.tracker.server.utils.mappers;

import com.assessment.tracker.server.persistence.entities.Assessment;
import com.assessment.tracker.server.persistence.entities.Module;
import com.assessment.tracker.server.utils.enums.AssessmentType;
import com.assessment.tracker.server.persistence.services.UserService;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.util.UUID;

@Component
public class CsvMapper implements Mapper<MultipartFile, List<Module>> {

    private UserService userService;

    public CsvMapper(UserService userService) {
    }

    @Override
    public MultipartFile entityToApi(List<Module> entity) {
        return null;
    }

    @Override
    public List<Module> apiToEntity(MultipartFile multipartFile) {
        List<Module> modules = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()))) {

            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] tokens = line.split(",");
                if (tokens.length < 4) {
                    continue;
                }

                Module module = new Module();
                module.setArchived(false);

                Integer code = parseIntOrNull(tokens[0].trim());
                if (code == null) {
                    continue;
                }
                module.setCode(code);

                module.setTitle(tokens[1].trim());

                String leaderName = tokens[2].trim();
                UUID leaderId = lookupUserId(leaderName);
                module.setLeaderID(leaderId);

                List<UUID> staffIds = parseSupportingStaff(tokens[3]);
                module.setOtherStaffIDs(staffIds);

                List<Assessment> assessments = parseAssessments(tokens, module);
                module.setAssessments(assessments);

                modules.add(module);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse CSV", e);
        }

        return modules;
    }


    // Helper functions
    private Integer parseIntOrNull(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private UUID lookupUserId(String username) {
        var user = userService.getUserByUsername(username);
        return user != null ? user.getUserID() : null;
    }

    private List<UUID> parseSupportingStaff(String raw) {
        List<UUID> ids = new ArrayList<>();
        if (raw == null || raw.trim().isEmpty()) {
            return ids;
        }

        String[] names = raw.split("\\|");

        for (String name : names) {
            name = name.trim();
            if (name.isEmpty()) {
                continue;
            }

            var user = userService.getUserByUsername(name);
            if (user != null) {
                ids.add(user.getUserID());
            }
        }

        return ids;
    }

    private List<Assessment> parseAssessments(String[] tokens, Module module) {
        List<Assessment> out = new ArrayList<>();

        for (int i = 4; i + 1 < tokens.length; i += 2) {
            String typeRaw = tokens[i].trim();
            String title = tokens[i + 1].trim();

            AssessmentType type = parseAssessmentType(typeRaw);
            if (type == null) {
                continue;
            }

            Assessment a = new Assessment();
            a.setTitle(title);
            a.setAssessmentType(type);
            a.setModule(module);
            a.setProgress(null);

            out.add(a);
        }

        return out;
    }

    private AssessmentType parseAssessmentType(String name) {
        try {
            return AssessmentType.valueOf(name.toUpperCase());
        } catch (Exception ignored) {
            return null;
        }
    }
}

