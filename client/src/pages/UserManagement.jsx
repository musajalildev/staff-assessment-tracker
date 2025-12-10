import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Layout from '../components/Layout';
import { userAPI, assignedUserAPI } from '../services/api';
import { getCurrentUser, canManageUsers } from '../utils/permissions';

function UserManagement() {
  const navigate = useNavigate();
  const [users, setUsers] = useState([]);
  const [assignedUsers, setAssignedUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [currentUser, setCurrentUser] = useState(null);

  useEffect(() => {
    const loadUsers = async () => {
      try {
        setLoading(true);
        const user = getCurrentUser();
        setCurrentUser(user);
        
        const [usersRes, assignedRes] = await Promise.all([
          userAPI.getAll(),
          assignedUserAPI.getAll().catch(() => ({ data: [] }))
        ]);
        setUsers(usersRes.data || []);
        setAssignedUsers(assignedRes.data || []);
      } catch (err) {
        setError('Failed to load users');
        console.error('Error loading users:', err);
      } finally {
        setLoading(false);
      }
    };

    loadUsers();
  }, []);

  const canManage = canManageUsers(
    assignedUsers,
    currentUser?.id || currentUser?.userID || currentUser?.ID,
    currentUser?.username,
    currentUser?.selectedUserType || currentUser?.selectedRole || currentUser?.primaryUserType
  );
  
  // Debug logging (remove in production)
  useEffect(() => {
    if (currentUser && assignedUsers.length > 0) {
      console.log('UserManagement permission check:', {
        currentUser,
        assignedUsers,
        canManage,
        userId: currentUser?.id || currentUser?.userID || currentUser?.ID,
        username: currentUser?.username,
        view: currentUser?.selectedUserType || currentUser?.selectedRole || currentUser?.primaryUserType
      });
    }
  }, [currentUser, assignedUsers, canManage]);

  const handlePromoteExamsOfficer = async (userId, username) => {
    if (!window.confirm(`Promote ${username} to Exams Officer?`)) return;
    
    try {
      // Check if user already has EXAM_OFFICER role
      const userAssignments = assignedUsers.filter(au => au.user?.userID === userId);
      const hasEO = userAssignments.some(au => au.role === 'EXAM_OFFICER');
      
      if (hasEO) {
        alert('User is already an Exams Officer');
        return;
      }

      await assignedUserAPI.create(userId, 'EXAM_OFFICER');
      // Reload
      const assignedRes = await assignedUserAPI.getAll();
      setAssignedUsers(assignedRes.data || []);
      alert('User promoted to Exams Officer');
    } catch (err) {
      alert('Failed to promote user');
      console.error('Error promoting user:', err);
    }
  };

  const handleDemoteExamsOfficer = async (assignmentId, userId, username) => {
    // Check if trying to demote self
    if (userId === (currentUser?.id || currentUser?.userID)) {
      alert('You cannot demote yourself. Another exams officer must demote you.');
      return;
    }

    if (!window.confirm(`Demote ${username} from Exams Officer?`)) return;
    
    try {
      await assignedUserAPI.delete(assignmentId);
      // Reload
      const assignedRes = await assignedUserAPI.getAll();
      setAssignedUsers(assignedRes.data || []);
      alert('User demoted from Exams Officer');
    } catch (err) {
      alert('Failed to demote user');
      console.error('Error demoting user:', err);
    }
  };

  const handleDeleteUser = async (userId, username) => {
    // Check if trying to delete current exams officer
    const userAssignments = assignedUsers.filter(au => au.user?.userID === userId);
    const isEO = userAssignments.some(au => au.role === 'EXAM_OFFICER');
    
    if (isEO) {
      if (!window.confirm(`Warning: ${username} is an Exams Officer. Are you sure you want to delete them?`)) {
        return;
      }
    } else {
      if (!window.confirm(`Are you sure you want to delete user "${username}"? This action cannot be undone.`)) {
        return;
      }
    }
    
    try {
      await userAPI.delete(userId);
      setUsers(users.filter(u => u.userID !== userId));
      alert('User deleted successfully');
    } catch (err) {
      alert('Failed to delete user');
      console.error('Error deleting user:', err);
    }
  };

  const getUserRoles = (userId) => {
    return assignedUsers
      .filter(au => au.user?.userID === userId)
      .map(au => au.role)
      .join(', ') || 'None';
  };

  const isExamOfficer = (user) => {
    return assignedUsers.some(au => 
      au.user?.userID === user.userID && au.role === 'EXAM_OFFICER'
    );
  };

  const getUserTypeLabel = (userType) => {
    if (!userType) return 'N/A';
    return userType.replace(/_/g, ' ').toLowerCase()
      .replace(/\b\w/g, l => l.toUpperCase());
  };

  // Check if user is Teaching Support Team
  const isTeachingSupport = currentUser?.userType === 'ROLE_TEACHING_SUPPORT' || 
                            currentUser?.selectedUserType === 'ROLE_TEACHING_SUPPORT' ||
                            currentUser?.primaryUserType === 'ROLE_TEACHING_SUPPORT';

  // Show Add User button if user can manage users OR is Teaching Support Team
  const canAddUser = canManage || isTeachingSupport;

  if (loading) {
    return (
      <Layout>
        <div className="card">
          <p>Loading users...</p>
        </div>
      </Layout>
    );
  }

  return (
    <Layout>
      <header className="header">
        <div className="h-title">User Management</div>
        <div className="actions">
          {canAddUser && (
            <button className="btn primary" onClick={() => navigate('/users/new')}>
              Add User
            </button>
          )}
        </div>
      </header>

      {error && (
        <div className="card" style={{ background: '#fee', color: '#c00', marginBottom: '16px' }}>
          {error}
        </div>
      )}

      <section className="card">
        <table className="table">
          <thead>
            <tr>
              <th>Username</th>
              <th>Email</th>
              <th>User Type</th>
              <th>Roles</th>
              <th>Exam Officer?</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {users.length > 0 ? (
              users.map((user) => (
                <tr key={user.userID}>
                  <td>{user.username}</td>
                  <td>{user.email}</td>
                  <td>{getUserTypeLabel(user.userType)}</td>
                  <td>{getUserRoles(user.userID)}</td>
                  <td>{isExamOfficer(user) ? 'Yes' : 'No'}</td>
                  <td>
                    <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
                      {canManage && (
                        <>
                          {isExamOfficer(user) ? (
                            <button
                              className="btn"
                              onClick={() => {
                                const assignment = assignedUsers.find(au => 
                                  au.user?.userID === user.userID && au.role === 'EXAM_OFFICER'
                                );
                                if (assignment) {
                                  handleDemoteExamsOfficer(assignment.assignmentID, user.userID, user.username);
                                }
                              }}
                              style={{ padding: '4px 8px', fontSize: '12px', color: 'var(--bad)' }}
                            >
                              Demote EO
                            </button>
                          ) : (
                            <button
                              className="btn"
                              onClick={() => handlePromoteExamsOfficer(user.userID, user.username)}
                              style={{ padding: '4px 8px', fontSize: '12px' }}
                            >
                              Promote EO
                            </button>
                          )}
                          <button
                            className="btn"
                            onClick={() => handleDeleteUser(user.userID, user.username)}
                            style={{ padding: '4px 8px', fontSize: '12px', color: 'var(--bad)' }}
                          >
                            Delete
                          </button>
                        </>
                      )}
                    </div>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="6" style={{ textAlign: 'center', padding: '24px' }}>
                  <p className="sub">No users found.</p>
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </section>
    </Layout>
  );
}

export default UserManagement;

