import { Link, useLocation } from 'react-router-dom';
import { useState, useEffect } from 'react';
import { assignedUserAPI } from '../services/api';
import { getCurrentUser, canManageUsers, isTeachingSupport, isExamsOfficer } from '../utils/permissions';

function Sidebar() {
  const location = useLocation();
  const [currentUser, setCurrentUser] = useState(null);
  const [assignedUsers, setAssignedUsers] = useState([]);
  const [canManage, setCanManage] = useState(false);

  useEffect(() => {
    const user = getCurrentUser();
    setCurrentUser(user);
    
    const loadData = async () => {
      try {
        const assignedRes = await assignedUserAPI.getAll().catch(() => ({ data: [] }));
        setAssignedUsers(assignedRes.data || []);
        
        if (user) {
          const userId = user.id || user.userID || user.ID;
          const username = user.username;
          const currentView = user.selectedUserType || user.selectedRole || user.primaryUserType;
          
          const canManageUsersFlag = canManageUsers(
            assignedRes.data || [],
            userId,
            username,
            currentView
          );
          setCanManage(canManageUsersFlag);
          
          // Debug logging (remove in production)
          console.log('Sidebar permission check:', {
            userId,
            username,
            currentView,
            assignedUsers: assignedRes.data,
            canManage: canManageUsersFlag
          });
        }
      } catch (err) {
        console.error('Error loading sidebar data:', err);
      }
    };
    loadData();
  }, [location.pathname]); // Re-check when route changes

  const isActive = (path) => {
    return location.pathname === path ? 'active' : '';
  };

  return (
    <aside className="sidebar">
      <div className="brand">
        <div className="logo"></div>
        <div>Assessment Tool</div>
      </div>
      <nav className="nav">
        <Link to="/dashboard" className={isActive('/dashboard')}>
          Dashboard
        </Link>
        <Link to="/modules" className={isActive('/modules')}>
          Modules
        </Link>
        {(canManage || isTeachingSupport(assignedUsers, currentUser?.id || currentUser?.userID || currentUser?.ID, currentUser?.username) || isExamsOfficer(assignedUsers, currentUser?.id || currentUser?.userID || currentUser?.ID, currentUser?.username)) && (
          <Link to="/users" className={isActive('/users')}>
            Users
          </Link>
        )}
        <Link to="/profile" className={isActive('/profile')}>
          My Profile
        </Link>
        <Link to="/login">Logout</Link>
      </nav>
    </aside>
  );
}

export default Sidebar;

