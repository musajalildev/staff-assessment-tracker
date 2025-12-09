// Workflow definitions for different assessment types

export const COURSEWORK_STAGES = [
  { value: 'CREATED', label: 'Spec Created', description: 'By Setter' },
  { value: 'CHECKED', label: 'Spec Checked', description: 'By Checker' },
  { value: 'NEEDS_CHANGES', label: 'Modifications Made', description: 'Loop between setter & checker' },
  { value: 'SPEC_RELEASED', label: 'Spec Released', description: 'Released to students' },
  { value: 'DEADLINE_PASSED', label: 'Deadline Passed', description: 'Submission deadline passed' },
  { value: 'MARKING_STANDARDISED', label: 'Standardisation', description: 'If team-marked' },
  { value: 'MARKED', label: 'Marking', description: 'Marking completed' },
  { value: 'MODERATION', label: 'Moderation', description: 'Moderation completed' },
  { value: 'FEEDBACK_RETURNED', label: 'Feedback Returned', description: 'Feedback returned to students' },
  { value: 'COMPLETE', label: 'Complete', description: 'Assessment finished' }
];

export const TEST_STAGES = [
  { value: 'CREATED', label: 'Test Created', description: 'By Setter' },
  { value: 'CHECKED', label: 'Checked', description: 'By Checker' },
  { value: 'NEEDS_CHANGES', label: 'Modifications', description: 'Loop between setter & checker' },
  { value: 'TEST_TAKING_PLACE', label: 'Test Takes Place', description: 'Test window active' },
  { value: 'MARKING_STANDARDISED', label: 'Standardisation', description: 'If team-marked' },
  { value: 'MARKED', label: 'Marking', description: 'If not autograded' },
  { value: 'MODERATION', label: 'Moderation', description: 'If not autograded' },
  { value: 'RESULTS_RETURNED', label: 'Results Returned', description: 'Results returned to students' },
  { value: 'COMPLETE', label: 'Complete', description: 'Assessment finished' }
];

export const EXAM_STAGES = [
  { value: 'CREATED', label: 'Exam Created', description: 'By Setter' },
  { value: 'CHECKED', label: 'Checked', description: 'By Checker' },
  { value: 'SETTER_MODIFICATIONS', label: 'Setter Modifications', description: 'After checker feedback' },
  { value: 'EXAMS_OFFICER_CHECK', label: 'Exams Officer Check', description: 'Initial review' },
  { value: 'SETTER_EO_APPROVAL', label: 'Setter Modifications + EO Approval', description: 'Final changes' },
  { value: 'EXTERNAL_EXAMINER_FEEDBACK', label: 'External Examiner Feedback', description: 'External examiner reviews' },
  { value: 'SETTER_RESPONSE', label: 'Setter Response', description: 'Formal response to feedback' },
  { value: 'CHECKER_FINAL_CHECK', label: 'Checker Final Check', description: 'Checker final checking before printing' },
  { value: 'SENT_TO_PRINT', label: 'Sent to Print', description: 'Paper sent to exams office' },
  { value: 'TEST_TAKING_PLACE', label: 'Exam Takes Place', description: 'Exam date' },
  { value: 'MARKING_STANDARDISED', label: 'Standardisation', description: 'If team-marked' },
  { value: 'MARKED', label: 'Marking', description: 'Marking completed' },
  { value: 'ADMIN_CHECK_MARKING_TOTALS', label: 'Admin Check Marking Totals', description: 'Teaching support verifies totals' },
  { value: 'MODERATION', label: 'Moderation', description: 'Moderation completed' },
  { value: 'COMPLETE', label: 'Complete', description: 'Assessment finished' }
];

/**
 * Get workflow stages for an assessment type
 */
export const getWorkflowStages = (assessmentType) => {
  switch (assessmentType) {
    case 'COURSEWORK':
      return COURSEWORK_STAGES;
    case 'EXAM':
      return EXAM_STAGES;
    case 'TEST_AUTOGRADED':
    case 'TEST_SINGLE_MARKER':
    case 'TEST_TEAM_MARKER':
      return TEST_STAGES;
    default:
      return TEST_STAGES; // Default fallback
  }
};

/**
 * Get the next stage in the workflow
 */
export const getNextStage = (currentStage, assessmentType) => {
  const stages = getWorkflowStages(assessmentType);
  const currentIndex = stages.findIndex(s => s.value === currentStage);
  if (currentIndex >= 0 && currentIndex < stages.length - 1) {
    return stages[currentIndex + 1].value;
  }
  return null;
};

/**
 * Get the previous stage in the workflow
 */
export const getPreviousStage = (currentStage, assessmentType) => {
  const stages = getWorkflowStages(assessmentType);
  const currentIndex = stages.findIndex(s => s.value === currentStage);
  if (currentIndex > 0) {
    return stages[currentIndex - 1].value;
  }
  return null;
};

/**
 * Get stage info by value
 */
export const getStageInfo = (stageValue, assessmentType) => {
  const stages = getWorkflowStages(assessmentType);
  return stages.find(s => s.value === stageValue) || { value: stageValue, label: stageValue, description: '' };
};

/**
 * Check if a stage can be progressed by a user role
 */
export const canProgressStage = (currentStage, assessmentType, userRole, isSetter, isChecker, isModerator, isModuleStaff, canOverride) => {
  if (canOverride) return true; // Admin/Exams Officer can always progress
  
  const stages = getWorkflowStages(assessmentType);
  const stageIndex = stages.findIndex(s => s.value === currentStage);
  
  // Basic role-based checks (simplified - would need more detailed logic)
  if (assessmentType === 'COURSEWORK') {
    if (currentStage === 'CREATED' && isSetter) return true;
    if (currentStage === 'CHECKED' && isChecker) return true;
    if (currentStage === 'NEEDS_CHANGES' && (isSetter || isChecker)) return true;
    if (currentStage === 'SPEC_RELEASED' && isModuleStaff) return true;
    if (currentStage === 'DEADLINE_PASSED' && isModuleStaff) return true;
    if (currentStage === 'MARKING_STANDARDISED' && isModuleStaff) return true;
    if (currentStage === 'MARKED' && isModuleStaff) return true;
    if (currentStage === 'MODERATION' && isModerator) return true;
    if (currentStage === 'FEEDBACK_RETURNED' && isModuleStaff) return true;
  } else if (assessmentType === 'EXAM') {
    if (currentStage === 'CREATED' && isSetter) return true;
    if (currentStage === 'CHECKED' && isChecker) return true;
    if (currentStage === 'SETTER_MODIFICATIONS' && isSetter) return true;
    if (currentStage === 'EXAMS_OFFICER_CHECK' && userRole === 'EXAM_OFFICER') return true;
    if (currentStage === 'SETTER_EO_APPROVAL' && (isSetter || userRole === 'EXAM_OFFICER')) return true;
    if (currentStage === 'EXTERNAL_EXAMINER_FEEDBACK' && userRole === 'EXTERNAL_EXAMINER') return true;
    if (currentStage === 'SETTER_RESPONSE' && isSetter) return true;
    if (currentStage === 'CHECKER_FINAL_CHECK' && isChecker) return true;
    if (currentStage === 'MARKED' && isModuleStaff) return true;
    if (currentStage === 'ADMIN_CHECK_MARKING_TOTALS' && userRole === 'TEACHING_SUPPORT') return true;
    if (currentStage === 'MODERATION' && isModerator) return true;
  } else {
    // Test workflows
    if (currentStage === 'CREATED' && isSetter) return true;
    if (currentStage === 'CHECKED' && isChecker) return true;
    if (currentStage === 'NEEDS_CHANGES' && (isSetter || isChecker)) return true;
    if (currentStage === 'TEST_TAKING_PLACE' && isModuleStaff) return true;
    if (currentStage === 'MARKING_STANDARDISED' && isModuleStaff) return true;
    if (currentStage === 'MARKED' && isModuleStaff) return true;
    if (currentStage === 'MODERATION' && isModuleStaff) return true;
    if (currentStage === 'RESULTS_RETURNED' && isModuleStaff) return true;
  }
  
  return false;
};

