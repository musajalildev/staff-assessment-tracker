import {Link, useLocation, useNavigate} from 'react-router-dom';
import { useState, useEffect } from 'react';
import { assignedUserAPI } from '../services/api';
import { getCurrentUser, canManageUsers } from '../utils/permissions';

function Sidebar() {
  const location = useLocation();
  const [currentUser, setCurrentUser] = useState(null);
  const [assignedUsers, setAssignedUsers] = useState([]);
  const [canManage, setCanManage] = useState(false);
  const navigate = useNavigate();
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

  // Check if user is Teaching Support Team
  const isTeachingSupport = currentUser?.userType === 'ROLE_TEACHING_SUPPORT' || 
                            currentUser?.selectedUserType === 'ROLE_TEACHING_SUPPORT' ||
                            currentUser?.primaryUserType === 'ROLE_TEACHING_SUPPORT';

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
        {/* Show Users link if user can manage users OR if they are Teaching Support Team */}
        {(canManage || isTeachingSupport) && (
          <Link to="/users" className={isActive('/users')}>
            Users
          </Link>
        )}
        <Link to="/profile" className={isActive('/profile')}>
          My Profile
        </Link>
      </nav>
      <div className="nav-footer">
        <button onClick={()=>{localStorage.removeItem("authToken"); navigate("/login")}}
                className="logout-link">Logout</button>
      </div>
    </aside>
  );
}

export default Sidebar;

