package com.assessment.tracker.server.app.mappers;

import com.assessment.tracker.server.persistence.entities.Assessment;
import com.assessment.tracker.server.persistence.entities.Module;
import com.assessment.tracker.server.utils.Mapper;
import com.assessment.tracker.server.persistence.services.AssessmentService;
import com.assessment.tracker.server.utils.enums.AssessmentProgress;
import com.assessment.tracker.server.utils.enums.AssessmentType;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;

/**
 * Mapper to convert between CSV and a list of Modules
 */
@Component
public class CsvMapper implements Mapper<MultipartFile, List<Module>> {

    private final AssessmentService assessmentService;

    public CsvMapper(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
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
                List<String> fields = new ArrayList<>(Arrays.asList(line.split(",")));

                int totalFields = fields.size();
                if (totalFields < 4) {
                    continue;
                }

                String code = fields.get(0).trim();
                if (code.isEmpty()) {
                    continue;
                }
                String title = fields.get(1).trim();
                if (title.isEmpty()) {
                    continue;
                }
                String moduleLead = fields.get(2).trim();
                if (moduleLead.isEmpty()) {
                    continue;
                }
                String supportingStaff = fields.get(3).trim();
                if (supportingStaff.isEmpty()) {
                    continue;
                }

                Module module = new Module();
                module.setCode(code);
                module.setTitle(title);
                module.setArchived(false);

                if (totalFields > 5) {
                    totalFields = totalFields - (totalFields % 2);
                    System.out.println(totalFields);
                    saveAssessments(fields.subList(4, totalFields), module);
                }
                modules.add(module);
            }
            return modules;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveAssessments(List<String> fields, Module module) {
        for (int i = 0; i < fields.size(); i += 2) {
            String assessmentTypeStr = fields.get(i).trim();
            String assessmentTitle = fields.get(i + 1).trim();
            if (assessmentTypeStr.isEmpty() || assessmentTitle.isEmpty()) {
                continue;
            }

            AssessmentType assessmentType = AssessmentType.fromString(assessmentTypeStr);

            Assessment assessment = new Assessment(module, assessmentType, AssessmentProgress.CREATED, assessmentTitle);
            assessmentService.save(assessment);
        }
    }
}
