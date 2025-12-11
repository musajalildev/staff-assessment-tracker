import { useState, useEffect } from 'react';
import Sidebar from './Sidebar';

function Layout({ children }) {
  const [currentUser, setCurrentUser] = useState(null);

  useEffect(() => {
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      setCurrentUser(JSON.parse(storedUser));
    }
  }, []);

  // Format user type for display
  const formatUserTypeDisplay = (userType) => {
    if (userType === 'ROLE_EXAMS_OFFICER') {
      return 'Exam Officer View (Teaching Support Team)';
    }
    if (userType === 'ROLE_ACADEMIC') {
      return 'Academic View';
    }
    if (userType === 'ROLE_TEACHING_SUPPORT') {
      return 'Teaching Support';
    }
    if (userType === 'ROLE_EXTERNAL_EXAMINER') {
      return 'External Examiner';
    }
    return userType
      .replace('ROLE_', '')
      .replace(/_/g, ' ')
      .toLowerCase()
      .replace(/\b\w/g, l => l.toUpperCase());
  };

  // Check if current user is an exams officer (based on their primary user type, not selected view)
  // An exams officer is someone whose PRIMARY user type is ROLE_EXAMS_OFFICER
  // They can switch between Academic View and Exam Officer View
  const userIsExamsOfficer = currentUser?.primaryUserType === 'ROLE_EXAMS_OFFICER' ||
                             currentUser?.userType === 'ROLE_EXAMS_OFFICER';

  // Get current view label for Exams Officers
  // Default to Exam Officer View if no view is explicitly selected
  const getCurrentViewLabel = () => {
    if (!currentUser || !userIsExamsOfficer) {
      return null;
    }
    
    // Determine current view based on selectedUserType
    const selectedView = currentUser.selectedUserType || currentUser.selectedRole;
    
    // If selectedUserType is ROLE_ACADEMIC, they're in Academic View
    // Otherwise (ROLE_EXAMS_OFFICER or undefined), they're in Exam Officer View (default)
    if (selectedView === 'ROLE_ACADEMIC' || selectedView === 'ACADEMIC') {
      return 'Academic View';
    }
    // Default to Exam Officer View (Teaching Support Team view)
    return 'Exam Officer View (Teaching Support Team)';
  };

  // Handle view switching for Exams Officers
  const handleSwitchView = () => {
    if (!currentUser || !userIsExamsOfficer) {
      return;
    }

    // Get current selected view, defaulting to Exam Officer View if not set
    const currentView = currentUser.selectedUserType || currentUser.selectedRole;
    const isCurrentlyAcademicView = currentView === 'ROLE_ACADEMIC' || currentView === 'ACADEMIC';
    
    // Toggle between Academic and Exam Officer views
    const newView = isCurrentlyAcademicView 
      ? 'ROLE_EXAMS_OFFICER' 
      : 'ROLE_ACADEMIC';

    const updatedUser = {
      ...currentUser,
      selectedUserType: newView,
      // Keep selectedRole in sync for compatibility
      selectedRole: newView === 'ROLE_EXAMS_OFFICER' ? 'EXAM_OFFICER' : 'ACADEMIC'
    };

    localStorage.setItem('currentUser', JSON.stringify(updatedUser));
    setCurrentUser(updatedUser);
    // Reload the page to apply changes across all components
    window.location.reload();
  };

  const currentView = getCurrentViewLabel();

  return (
    <div className="app">
      <Sidebar />
      <main className="content">
        {currentUser && (
          <div style={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            padding: '12px 0',
            marginBottom: '16px',
            borderBottom: '1px solid var(--border)'
          }}>
            <div style={{ color: 'white', fontSize: '14px', fontWeight: '600' }}>
              {userIsExamsOfficer ? (
                <>Exam Officer: {currentView || 'Exam Officer View (Teaching Support Team)'}</>
              ) : (
                formatUserTypeDisplay(currentUser.selectedUserType || currentUser.selectedRole || currentUser.userType || currentUser.primaryUserType)
              )}
            </div>
            {userIsExamsOfficer && (
              <button
                className="btn"
                onClick={handleSwitchView}
                style={{
                  padding: '8px 16px',
                  fontSize: '12px',
                  whiteSpace: 'nowrap'
                }}
              >
                Switch to {(() => {
                  const selectedView = currentUser.selectedUserType || currentUser.selectedRole;
                  const isAcademicView = selectedView === 'ROLE_ACADEMIC' || selectedView === 'ACADEMIC';
                  return isAcademicView ? 'Exam Officer View' : 'Academic View';
                })()}
              </button>
            )}
          </div>
        )}
        {children}
      </main>
    </div>
  );
}

export default Layout;