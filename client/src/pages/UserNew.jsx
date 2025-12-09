import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Layout from '../components/Layout';
import { userAPI, assignedUserAPI } from '../services/api';
import { getCurrentUser, canManageUsers } from '../utils/permissions';

function UserNew() {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [currentUser, setCurrentUser] = useState(null);
  const [assignedUsers, setAssignedUsers] = useState([]);
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    userType: 'ACADEMIC',
    roles: []
  });

  useEffect(() => {
    const user = getCurrentUser();
    setCurrentUser(user);
    
    const loadData = async () => {
      try {
        const assignedRes = await assignedUserAPI.getAll().catch(() => ({ data: [] }));
        setAssignedUsers(assignedRes.data || []);
      } catch (err) {
        console.error('Error loading data:', err);
      }
    };
    loadData();
  }, []);

  const canManage = canManageUsers(
    assignedUsers,
    currentUser?.id || currentUser?.userID,
    currentUser?.username,
    currentUser?.selectedUserType || currentUser?.selectedRole
  );

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    if (type === 'checkbox') {
      const roles = formData.roles.includes(value)
        ? formData.roles.filter(r => r !== value)
        : [...formData.roles, value];
      setFormData({ ...formData, roles });
    } else {
      setFormData({ ...formData, [name]: value });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    if (!formData.username || !formData.email || !formData.password) {
      setError('Please fill in all required fields');
      setLoading(false);
      return;
    }

    try {
      // Create user
      const userData = {
        username: formData.username,
        email: formData.email,
        password: formData.password,
        userType: formData.userType
      };

      const userRes = await userAPI.create(userData);
      const newUser = userRes.data;

      // Assign roles
      if (formData.roles.length > 0) {
        for (const role of formData.roles) {
          try {
            await assignedUserAPI.create(newUser.userID, role);
          } catch (err) {
            console.error(`Error assigning role ${role}:`, err);
          }
        }
      }

      navigate('/users');
    } catch (err) {
      setError('Failed to create user. Please try again.');
      console.error('Error creating user:', err);
    } finally {
      setLoading(false);
    }
  };

  if (!canManage) {
    return (
      <Layout>
        <div className="card" style={{ background: 'rgba(255, 51, 102, 0.1)', borderColor: 'var(--bad)' }}>
          <h2 style={{ color: 'var(--bad)' }}>Access Denied</h2>
          <p>You don't have permission to create users.</p>
          <button className="btn mt-12" onClick={() => navigate('/users')}>Back to Users</button>
        </div>
      </Layout>
    );
  }

  return (
    <Layout>
      <header className="header">
        <div className="h-title">Create User</div>
        <div className="actions">
          <button className="btn" onClick={() => navigate('/users')}>Cancel</button>
          <button className="btn primary" onClick={handleSubmit} disabled={loading}>
            {loading ? 'Creating...' : 'Create User'}
          </button>
        </div>
      </header>

      <section className="card">
        <form className="form" onSubmit={handleSubmit}>
          <div className="grid two">
            <div className="field">
              <label className="label" htmlFor="username">Username *</label>
              <input
                className="input"
                id="username"
                name="username"
                value={formData.username}
                onChange={handleChange}
                required
              />
            </div>
            <div className="field">
              <label className="label" htmlFor="email">Email *</label>
              <input
                type="email"
                className="input"
                id="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                required
              />
            </div>
          </div>
          <div className="field">
            <label className="label" htmlFor="password">Password *</label>
            <input
              type="password"
              className="input"
              id="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required
              placeholder="User must change on first login"
            />
            <p className="sub mt-8" style={{ fontSize: '12px' }}>
              User will be required to change this password on first login.
            </p>
          </div>
          <div className="field">
            <label className="label" htmlFor="userType">User Type *</label>
            <select
              className="select"
              id="userType"
              name="userType"
              value={formData.userType}
              onChange={handleChange}
              required
            >
              <option value="ACADEMIC">Academic</option>
              <option value="TEACHING_SUPPORT">Teaching Support</option>
              <option value="EXTERNAL_EXAMINER">External Examiner</option>
            </select>
          </div>
          <div className="field">
            <label className="label">Additional Roles (Optional)</label>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', marginTop: '8px' }}>
              <label style={{ display: 'flex', alignItems: 'center', gap: '8px', cursor: 'pointer' }}>
                <input
                  type="checkbox"
                  value="EXAM_OFFICER"
                  checked={formData.roles.includes('EXAM_OFFICER')}
                  onChange={handleChange}
                />
                <span>Exams Officer</span>
              </label>
              <p className="sub" style={{ fontSize: '12px', marginLeft: '24px' }}>
                Exams Officer role can be assigned to academics. They can toggle between academic and admin views.
              </p>
            </div>
          </div>
          {error && (
            <div style={{ color: 'var(--bad)', marginTop: '12px' }}>{error}</div>
          )}
          <div className="field" style={{ marginTop: '24px' }}>
            <button 
              className="btn primary" 
              type="submit" 
              disabled={loading || !formData.username || !formData.email || !formData.password}
            >
              {loading ? 'Creating...' : 'Create User'}
            </button>
            <button 
              className="btn" 
              type="button"
              onClick={() => navigate('/users')}
              style={{ marginLeft: '12px' }}
            >
              Cancel
            </button>
          </div>
        </form>
      </section>
    </Layout>
  );
}

export default UserNew;

