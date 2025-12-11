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
    userType: 'ROLE_ACADEMIC'
  });

  useEffect(() => {
    const user = getCurrentUser();
    setCurrentUser(user);
    
    const loadData = async () => {
      try {
        const assignedRes = await assignedUserAPI.getAll();
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
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
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

      await userAPI.create(userData);
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
              <option value="ROLE_ACADEMIC">Academic</option>
              <option value="ROLE_TEACHING_SUPPORT">Teaching Support</option>
              <option value="ROLE_EXTERNAL_EXAMINER">External Examiner</option>
              <option value="ROLE_EXAMS_OFFICER">Exams Officer</option>
            </select>
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

