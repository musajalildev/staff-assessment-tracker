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

  // Get current view label for Exams Officers
  const getCurrentViewLabel = () => {
    if (!currentUser || currentUser.primaryUserType !== 'ROLE_EXAMS_OFFICER') {
      return null;
    }
    if (currentUser.selectedUserType === 'ROLE_ACADEMIC') {
      return 'Academic View';
    }
    return 'Exam Officer View (Teaching Support Team)';
  };

  // Handle view switching for Exams Officers
  const handleSwitchView = () => {
    if (!currentUser || currentUser.primaryUserType !== 'ROLE_EXAMS_OFFICER') {
      return;
    }

    const newView = currentUser.selectedUserType === 'ROLE_ACADEMIC' 
      ? 'ROLE_EXAMS_OFFICER' 
      : 'ROLE_ACADEMIC';

    const updatedUser = {
      ...currentUser,
      selectedUserType: newView
    };

    localStorage.setItem('currentUser', JSON.stringify(updatedUser));
    setCurrentUser(updatedUser);
    // Reload the page to apply changes
    window.location.reload();
  };

  const isExamsOfficer = currentUser?.primaryUserType === 'ROLE_EXAMS_OFFICER';
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
              {isExamsOfficer ? (
                <>Exam Officer: {currentView}</>
              ) : (
                formatUserTypeDisplay(currentUser.selectedUserType || currentUser.userType || currentUser.primaryUserType)
              )}
            </div>
            {isExamsOfficer && (
              <button
                className="btn"
                onClick={handleSwitchView}
                style={{
                  padding: '8px 16px',
                  fontSize: '12px',
                  whiteSpace: 'nowrap'
                }}
              >
                Switch to {currentUser.selectedUserType === 'ROLE_ACADEMIC' 
                  ? 'Exam Officer View' 
                  : 'Academic View'}
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

