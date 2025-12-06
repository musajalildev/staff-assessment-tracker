import { Link } from 'react-router-dom';
import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import { moduleAPI, userAPI } from '../services/api';

function Modules() {
  const [modules, setModules] = useState([]);
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const loadModules = async () => {
      try {
        setLoading(true);
        const [modulesRes, usersRes] = await Promise.all([
          moduleAPI.getAll().catch(() => ({ data: [] })),
          userAPI.getAll().catch(() => ({ data: [] }))
        ]);
        setModules(modulesRes.data || []);
        setUsers(usersRes.data || []);
      } catch (err) {
        setError('Failed to load modules');
        console.error('Error loading modules:', err);
      } finally {
        setLoading(false);
      }
    };

    loadModules();
  }, []);

  const getUserName = (userId) => {
    if (!userId) return 'N/A';
    const user = users.find(u => u.userID === userId || u.ID === userId);
    return user ? user.username : 'N/A';
  };

  const getAssessmentCount = (module) => {
    return module.assessments ? module.assessments.length : 0;
  };

  if (loading) {
    return (
      <Layout>
        <div className="card">
          <p>Loading modules...</p>
        </div>
      </Layout>
    );
  }

  return (
    <Layout>
      <header className="header">
        <div className="h-title">Modules</div>
        <div className="actions">
          <Link className="btn primary" to="/modules/new">Add Module</Link>
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
              <th>Code</th>
              <th>Title</th>
              <th>Lead</th>
              <th>Assessments</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {modules.length > 0 ? (
              modules.map((module) => (
                <tr key={module.id || module.ID}>
                  <td>{module.code || module.moduleCode || 'N/A'}</td>
                  <td>{module.title || 'Untitled Module'}</td>
                  <td>{getUserName(module.moduleLeaderID)}</td>
                  <td>{getAssessmentCount(module)}</td>
                  <td>
                    <Link to={`/modules/${module.id || module.ID}`}>Open →</Link>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="5" style={{ textAlign: 'center', padding: '24px' }}>
                  <p className="sub">No modules found.</p>
                  <Link className="btn primary mt-12" to="/modules/new" style={{ display: 'inline-block' }}>
                    Create your first module
                  </Link>
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </section>
    </Layout>
  );
}

export default Modules;

