import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Layout from '../components/Layout';
import { moduleAPI, userAPI, assignedUserAPI } from '../services/api';
import { getCurrentUser, canManageModules } from '../utils/permissions';

function ModuleNew() {
  const navigate = useNavigate();
  const [users, setUsers] = useState([]);
  const [assignedUsers, setAssignedUsers] = useState([]);
  const [currentUser, setCurrentUser] = useState(null);
  const [loading, setLoading] = useState(false);
  const [checkingPermissions, setCheckingPermissions] = useState(true);
  const [error, setError] = useState('');
  const [formData, setFormData] = useState({
    code: '',
    title: '',
    lead: '',
    moderator: '',
    staff: ''
  });

  useEffect(() => {
    const loadData = async () => {
      try {
        const user = getCurrentUser();
        setCurrentUser(user);
        
        const [usersRes, assignedRes] = await Promise.all([
          userAPI.getAll(),
          assignedUserAPI.getAll().catch(() => ({ data: [] }))
        ]);
        setUsers(usersRes.data || []);
        setAssignedUsers(assignedRes.data || []);
        
        // Check permissions
        const userId = user?.id || user?.userID || user?.ID;
        const username = user?.username;
        const currentView = user?.selectedUserType || user?.selectedRole || user?.primaryUserType;
        const canManage = canManageModules(assignedRes.data || [], userId, username, currentView);
        
        if (!canManage) {
          setError('You do not have permission to create modules.');
        }
        setCheckingPermissions(false);
      } catch (err) {
        console.error('Error loading data:', err);
        setCheckingPermissions(false);
      }
    };
    loadData();
  }, []);

  const canManage = canManageModules(
    assignedUsers,
    currentUser?.id || currentUser?.userID || currentUser?.ID,
    currentUser?.username,
    currentUser?.selectedUserType || currentUser?.selectedRole || currentUser?.primaryUserType
  );

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const moduleData = {
        code: formData.code,
        title: formData.title,
        moduleLeaderID: formData.lead ? parseInt(formData.lead) : null,
        archived: false
      };

      await moduleAPI.create(moduleData);
      navigate('/modules');
    } catch (err) {
      setError('Failed to create module. Please try again.');
      console.error('Error creating module:', err);
    } finally {
      setLoading(false);
    }
  };

  if (checkingPermissions) {
    return (
      <Layout>
        <div className="card">
          <p>Loading...</p>
        </div>
      </Layout>
    );
  }

  if (!canManage) {
    return (
      <Layout>
        <div className="card" style={{ background: 'rgba(255, 51, 102, 0.1)', borderColor: 'var(--bad)' }}>
          <h2 style={{ color: 'var(--bad)' }}>Access Denied</h2>
          <p>You do not have permission to create modules. Only Teaching Support staff and Exams Officers (in admin view) can create modules.</p>
          <button className="btn mt-12" onClick={() => navigate('/modules')}>Back to Modules</button>
        </div>
      </Layout>
    );
  }

  return (
    <Layout>
      <header className="header">
        <div className="h-title">Add Module</div>
        <div className="actions">
          <button className="btn" onClick={() => navigate('/modules')}>Cancel</button>
          <button className="btn primary" onClick={handleSubmit} disabled={loading || !formData.code || !formData.title}>
            {loading ? 'Creating...' : 'Save'}
          </button>
        </div>
      </header>

      <section className="card">
        <form className="form" onSubmit={handleSubmit}>
          <div className="grid two">
            <div className="field">
              <label className="label" htmlFor="code">Module Code</label>
              <input
                className="input"
                id="code"
                name="code"
                placeholder="e.g., CSC101"
                value={formData.code}
                onChange={handleChange}
              />
            </div>
            <div className="field">
              <label className="label" htmlFor="title">Title</label>
              <input
                className="input"
                id="title"
                name="title"
                placeholder="Programming 1"
                value={formData.title}
                onChange={handleChange}
              />
            </div>
          </div>
          <div className="grid two">
            <div className="field">
              <label className="label" htmlFor="lead">Module Lead</label>
              <select
                className="select"
                id="lead"
                name="lead"
                value={formData.lead}
                onChange={handleChange}
              >
                <option value="">Select...</option>
                {users.map((user) => (
                  <option key={user.userID} value={user.userID}>
                    {user.username} ({user.email})
                  </option>
                ))}
              </select>
            </div>
            <div className="field">
              <label className="label" htmlFor="moderator">Moderator (Optional)</label>
              <select
                className="select"
                id="moderator"
                name="moderator"
                value={formData.moderator}
                onChange={handleChange}
              >
                <option value="">Select...</option>
                {users.map((user) => (
                  <option key={user.userID} value={user.userID}>
                    {user.username} ({user.email})
                  </option>
                ))}
              </select>
            </div>
          </div>
          <div className="field">
            <label className="label" htmlFor="staff">Other Staff (comma separated - Optional)</label>
            <input
              className="input"
              id="staff"
              name="staff"
              placeholder="John Smith, Mary Chan"
              value={formData.staff}
              onChange={handleChange}
            />
          </div>
          {error && (
            <div style={{ color: 'red', marginTop: '12px' }}>{error}</div>
          )}
          <div className="field" style={{ marginTop: '24px' }}>
            <button 
              className="btn primary" 
              type="submit" 
              disabled={loading || !formData.code || !formData.title}
            >
              {loading ? 'Creating...' : 'Create Module'}
            </button>
            <button 
              className="btn" 
              type="button"
              onClick={() => navigate('/modules')}
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

export default ModuleNew;

