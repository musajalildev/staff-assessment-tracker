package com.assessment.tracker.server;

import com.assessment.tracker.server.persistence.entities.Assessment;
import com.assessment.tracker.server.persistence.entities.Module;
import com.assessment.tracker.server.persistence.services.AssessmentService;
import com.assessment.tracker.server.utils.enums.AssessmentType;
import com.assessment.tracker.server.utils.mappers.CsvMapper;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = { CsvMapper.class })
class CsvMapperSpringBootTest {
    @Autowired
    CsvMapper csvMapper;
    @MockitoBean
    AssessmentService assessmentService;

    @Test
    void parsesModulesAndSavesAssessments() {
        String csv =
                "CS101, Intro to CS, Alice Smith, Bob Jones, cw, CW1\n" +
                        "CS102, Data Structures, Carol White, Dan Brown, exam, Final Exam, cw, DS CW\n";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "modules.csv",
                "text/plain",
                csv.getBytes(StandardCharsets.UTF_8)
        );

        List<com.assessment.tracker.server.persistence.entities.Module> modules = csvMapper.apiToEntity(file);

        assertThat(modules).hasSize(2);

        com.assessment.tracker.server.persistence.entities.Module m1 = modules.get(0);
        AssertionsForClassTypes.assertThat(m1.getCode()).isEqualTo("CS101");

        Module m2 = modules.get(1);
        AssertionsForClassTypes.assertThat(m2.getCode()).isEqualTo("CS102");

        ArgumentCaptor<Assessment> captor = ArgumentCaptor.forClass(Assessment.class);
        verify(assessmentService, times(3)).save(captor.capture());

        List<Assessment> saved = captor.getAllValues();

        assertThat(saved.get(0).getAssessmentType()).isEqualTo(AssessmentType.COURSEWORK);
        AssertionsForClassTypes.assertThat(saved.get(0).getTitle()).isEqualTo("CW1");
        AssertionsForClassTypes.assertThat(saved.get(0).getModule()).isEqualTo(m1);

        assertThat(saved.get(1).getAssessmentType()).isEqualTo(AssessmentType.EXAM);
        assertThat(saved.get(2).getAssessmentType()).isEqualTo(AssessmentType.COURSEWORK);
    }

    @Test
    void skipsInvalidLines() throws Exception {
        String csv =
                "\n" +
                        ",,, \n" +
                        "CS101, Title Only, , \n" +
                        "CS200, Valid Module, Lead, Staff\n";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "modules.csv",
                "text/plain",
                csv.getBytes(StandardCharsets.UTF_8)
        );

        List<Module> modules = csvMapper.apiToEntity(file);

        assertThat(modules).hasSize(1);
        assertThat(modules.get(0).getCode()).isEqualTo("CS200");

        verify(assessmentService, never()).save(any());
    }
}
