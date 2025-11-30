import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import Layout from '../components/Layout';
import { moduleAPI, userAPI } from '../services/api';

function ModuleEdit() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [users, setUsers] = useState([]);
  const [module, setModule] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
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
        setLoading(true);
        const [usersRes] = await Promise.all([
          userAPI.getAll().catch(() => ({ data: [] }))
        ]);
        setUsers(usersRes.data || []);

        // Try to load module
        try {
          const moduleRes = await moduleAPI.getByCode(id);
          setModule(moduleRes.data);
          setFormData({
            code: moduleRes.data.code || moduleRes.data.moduleCode || '',
            title: moduleRes.data.title || '',
            lead: moduleRes.data.moduleLeaderID?.toString() || '',
            moderator: '',
            staff: ''
          });
        } catch (e) {
          const allModulesRes = await moduleAPI.getAll();
          const foundModule = (allModulesRes.data || []).find(
            m => (m.id || m.ID)?.toString() === id
          );
          if (foundModule) {
            setModule(foundModule);
            setFormData({
              code: foundModule.code || foundModule.moduleCode || '',
              title: foundModule.title || '',
              lead: foundModule.moduleLeaderID?.toString() || '',
              moderator: '',
              staff: ''
            });
          } else {
            setError('Module not found');
          }
        }
      } catch (err) {
        setError('Failed to load module');
        console.error('Error loading module:', err);
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, [id]);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSaving(true);

    try {
      const moduleData = {
        id: module?.id || module?.ID,
        code: formData.code,
        title: formData.title,
        moduleLeaderID: formData.lead ? parseInt(formData.lead) : null,
        archived: module?.archived || false
      };

      await moduleAPI.update(moduleData);
      navigate(`/modules/${id}`);
    } catch (err) {
      setError('Failed to update module. Please try again.');
      console.error('Error updating module:', err);
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <Layout>
        <div className="card">
          <p>Loading module...</p>
        </div>
      </Layout>
    );
  }

  if (error && !module) {
    return (
      <Layout>
        <div className="content">
          <h2>{error}</h2>
          <button className="btn" onClick={() => navigate('/modules')}>Back to Modules</button>
        </div>
      </Layout>
    );
  }

  return (
    <Layout>
      <header className="header">
        <div className="h-title">Edit Module ({formData.code || 'N/A'})</div>
        <div className="actions">
          <button className="btn" onClick={() => navigate(`/modules/${id}`)}>Cancel</button>
          <button className="btn primary" onClick={handleSubmit} disabled={saving || !formData.code || !formData.title}>
            {saving ? 'Saving...' : 'Save'}
          </button>
        </div>
      </header>

      <section className="card">
        <form className="form" onSubmit={handleSubmit}>
          <div className="grid two">
            <div className="field">
              <label className="label" htmlFor="code2">Module Code</label>
              <input
                className="input"
                id="code2"
                name="code"
                value={formData.code}
                onChange={handleChange}
              />
            </div>
            <div className="field">
              <label className="label" htmlFor="title2">Title</label>
              <input
                className="input"
                id="title2"
                name="title"
                value={formData.title}
                onChange={handleChange}
              />
            </div>
          </div>
          <div className="grid two">
            <div className="field">
              <label className="label" htmlFor="lead2">Module Lead</label>
              <select
                className="select"
                id="lead2"
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
              <label className="label" htmlFor="moderator2">Moderator (Optional)</label>
              <select
                className="select"
                id="moderator2"
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
            <label className="label" htmlFor="staff2">Other Staff (Optional)</label>
            <input
              className="input"
              id="staff2"
              name="staff"
              value={formData.staff}
              onChange={handleChange}
              placeholder="Comma separated list"
            />
          </div>
          {error && (
            <div style={{ color: 'red', marginTop: '12px' }}>{error}</div>
          )}
        </form>
      </section>
    </Layout>
  );
}

export default ModuleEdit;

