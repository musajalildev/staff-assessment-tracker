import { useState, useEffect } from 'react';
import Sidebar from './Sidebar';
import { assignedUserAPI } from '../services/api';
import { isExamsOfficer } from '../utils/permissions';

function Layout({ children }) {
  const [currentUser, setCurrentUser] = useState(null);
  const [assignedUsers, setAssignedUsers] = useState([]);

  useEffect(() => {
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      setCurrentUser(JSON.parse(storedUser));
    }
    
    // Load assigned users to check for exams officer role
    const loadAssignedUsers = async () => {
      try {
        const assignedRes = await assignedUserAPI.getAll().catch(() => ({ data: [] }));
        setAssignedUsers(assignedRes.data || []);
      } catch (err) {
        console.error('Error loading assigned users:', err);
      }
    };
    loadAssignedUsers();
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

  // Check if current user is an exams officer based on assigned roles
  const userId = currentUser?.id || currentUser?.userID || currentUser?.ID;
  const username = currentUser?.username;
  const userIsExamsOfficer = isExamsOfficer(assignedUsers, userId, username);

  // Get current view label for Exams Officers
  const getCurrentViewLabel = () => {
    if (!currentUser || !userIsExamsOfficer) {
      return null;
    }
    if (currentUser.selectedUserType === 'ROLE_ACADEMIC' || currentUser.selectedRole === 'ACADEMIC') {
      return 'Academic View';
    }
    return 'Exam Officer View (Teaching Support Team)';
  };

  // Handle view switching for Exams Officers
  const handleSwitchView = () => {
    if (!currentUser || !userIsExamsOfficer) {
      return;
    }

    const currentView = currentUser.selectedUserType || currentUser.selectedRole;
    const newView = (currentView === 'ROLE_ACADEMIC' || currentView === 'ACADEMIC')
      ? 'ROLE_EXAMS_OFFICER' 
      : 'ROLE_ACADEMIC';

    const updatedUser = {
      ...currentUser,
      selectedUserType: newView,
      selectedRole: newView === 'ROLE_EXAMS_OFFICER' ? 'EXAM_OFFICER' : 'ACADEMIC'
    };

    localStorage.setItem('currentUser', JSON.stringify(updatedUser));
    setCurrentUser(updatedUser);
    // Reload the page to apply changes
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
                <>Exam Officer: {currentView || 'Exam Officer View'}</>
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
                Switch to {(currentUser.selectedUserType === 'ROLE_ACADEMIC' || currentUser.selectedRole === 'ACADEMIC')
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