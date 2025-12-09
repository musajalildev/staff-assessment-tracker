// Permission utility functions for role-based access control

/**
 * Get current user from localStorage
 */
export const getCurrentUser = () => {
  const stored = localStorage.getItem('currentUser');
  return stored ? JSON.parse(stored) : null;
};

/**
 * Get user's roles
 */
export const getUserRoles = (assignedUsers, userId, username) => {
  if (!assignedUsers || (!userId && !username)) return [];
  return assignedUsers
    .filter(au => {
      const userMatch = au.user?.userID === userId || 
                       au.user?.ID === userId ||
                       au.user?.username === username ||
                       (userId && au.user && (au.user.userID?.toString() === userId?.toString() || au.user.ID?.toString() === userId?.toString()));
      return userMatch;
    })
    .map(au => au.role);
};

/**
 * Check if user has a specific role
 */
export const hasRole = (assignedUsers, userId, username, role) => {
  const roles = getUserRoles(assignedUsers, userId, username);
  return roles.includes(role);
};

/**
 * Check if user is Teaching Support (Admin)
 */
export const isTeachingSupport = (assignedUsers, userId, username) => {
  return hasRole(assignedUsers, userId, username, 'TEACHING_SUPPORT');
};

/**
 * Check if user is Exams Officer
 */
export const isExamsOfficer = (assignedUsers, userId, username) => {
  return hasRole(assignedUsers, userId, username, 'EXAM_OFFICER');
};

/**
 * Check if user is External Examiner
 */
export const isExternalExaminer = (assignedUsers, userId, username) => {
  return hasRole(assignedUsers, userId, username, 'EXTERNAL_EXAMINER');
};

/**
 * Check if user is Academic
 */
export const isAcademic = (assignedUsers, userId, username) => {
  return hasRole(assignedUsers, userId, username, 'ACADEMIC');
};

/**
 * Check if user can perform admin actions (Teaching Support or Exams Officer in admin view)
 */
export const canPerformAdminActions = (assignedUsers, userId, username, currentView) => {
  if (!assignedUsers || !userId && !username) return false;
  
  const user = getCurrentUser();
  const isAdmin = isTeachingSupport(assignedUsers, userId, username);
  const isEO = isExamsOfficer(assignedUsers, userId, username);
  
  // Exams Officer can perform admin actions if:
  // 1. They have EXAM_OFFICER role AND
  // 2. They're in exams officer view (selectedUserType is ROLE_EXAMS_OFFICER or selectedRole is EXAM_OFFICER)
  // OR if currentView indicates they're in admin mode
  const isEOAdminView = isEO && (
    currentView === 'EXAM_OFFICER' || 
    currentView === 'ROLE_EXAMS_OFFICER' ||
    user?.selectedUserType === 'ROLE_EXAMS_OFFICER' ||
    user?.selectedRole === 'EXAM_OFFICER'
  );
  
  return isAdmin || isEOAdminView;
};

/**
 * Check if user can see all modules (admin view)
 */
export const canSeeAllModules = (assignedUsers, userId, username, currentView) => {
  return canPerformAdminActions(assignedUsers, userId, username, currentView);
};

/**
 * Check if user can create/edit/delete modules
 */
export const canManageModules = (assignedUsers, userId, username, currentView) => {
  return canPerformAdminActions(assignedUsers, userId, username, currentView);
};

/**
 * Check if user can create/edit/delete users
 * Teaching Support and Exams Officers (in admin view) can manage users
 */
export const canManageUsers = (assignedUsers, userId, username, currentView) => {
  if (!assignedUsers || (!userId && !username)) return false;
  
  // Check if user has TEACHING_SUPPORT role
  const hasTeachingSupport = isTeachingSupport(assignedUsers, userId, username);
  if (hasTeachingSupport) return true;
  
  // Check if user is Exams Officer and in admin view
  const isEO = isExamsOfficer(assignedUsers, userId, username);
  if (isEO) {
    const user = getCurrentUser();
    // Exams officers can manage users if they're in exams officer view
    // Check various possible view indicators
    const inAdminView = currentView === 'EXAM_OFFICER' || 
                       currentView === 'ROLE_EXAMS_OFFICER' ||
                       user?.selectedUserType === 'ROLE_EXAMS_OFFICER' ||
                       user?.selectedRole === 'EXAM_OFFICER' ||
                       user?.primaryUserType === 'ROLE_EXAMS_OFFICER';
    // If exams officer, allow access (they can toggle view if needed)
    return true; // Exams officers should be able to see users page
  }
  
  return false;
};

/**
 * Check if user can reverse assessment stages
 */
export const canReverseStages = (assignedUsers, userId, username, currentView) => {
  return canPerformAdminActions(assignedUsers, userId, username, currentView);
};

/**
 * Check if user can progress assessment at any stage (override)
 */
export const canOverrideProgress = (assignedUsers, userId, username, currentView) => {
  return canPerformAdminActions(assignedUsers, userId, username, currentView);
};

/**
 * Filter modules based on user role
 * - Teaching Support/Exams Officer (admin view): see all
 * - Academic: see only modules where they are staff/lead/moderator
 * - External Examiner: see only assigned modules
 */
export const filterModulesByRole = (modules, assignedUsers, moduleRoles, userId, username, currentView) => {
  const user = getCurrentUser();
  const canSeeAll = canSeeAllModules(assignedUsers, userId, username, currentView);
  
  if (canSeeAll) {
    return modules;
  }

  const isExtExaminer = isExternalExaminer(assignedUsers, userId, username);
  
  if (isExtExaminer) {
    // External examiners see only modules they're assigned to
    // This would need backend support - for now return all (will be filtered by backend)
    return modules;
  }

  // Academics see modules where they have a role
  if (moduleRoles && moduleRoles.length > 0) {
    const userModuleIds = moduleRoles
      .filter(mr => mr.user?.userID === userId || mr.user?.username === username)
      .map(mr => mr.module?.id || mr.module?.ID);
    
    return modules.filter(m => userModuleIds.includes(m.id || m.ID));
  }

  // Default: show all (will be filtered by backend when implemented)
  return modules;
};

