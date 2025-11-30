import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import { userAPI, assignedUserAPI } from '../services/api';

function UserManagement() {
  const [users, setUsers] = useState([]);
  const [assignedUsers, setAssignedUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const loadUsers = async () => {
      try {
        setLoading(true);
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
          <button className="btn primary" onClick={() => alert('User creation form coming soon')}>
            Add User
          </button>
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
                    <button 
                      className="btn" 
                      onClick={() => alert(`Edit user ${user.username} - Feature coming soon`)}
                      style={{ padding: '4px 8px', fontSize: '14px' }}
                    >
                      Edit
                    </button>
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

