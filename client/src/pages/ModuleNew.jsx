import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Layout from '../components/Layout';
import { moduleAPI, userAPI } from '../services/api';

function ModuleNew() {
  const navigate = useNavigate();
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [formData, setFormData] = useState({
    code: '',
    title: '',
    lead: '',
    moderator: '',
    staff: ''
  });

  useEffect(() => {
    const loadUsers = async () => {
      try {
        const usersRes = await userAPI.getAll();
        setUsers(usersRes.data || []);
      } catch (err) {
        console.error('Error loading users:', err);
      }
    };
    loadUsers();
  }, []);

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

  return (
    <Layout>
      <header className="header">
        <div className="h-title">Add Module</div>
        <div className="actions">
          <button className="btn" onClick={() => navigate('/modules')}>Cancel</button>
          <button className="btn primary" onClick={handleSubmit}>Save</button>
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

