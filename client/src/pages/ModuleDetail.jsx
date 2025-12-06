import { Link, useParams } from 'react-router-dom';
import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import { moduleAPI, userAPI, assessmentAPI } from '../services/api';

function ModuleDetail() {
  const { id } = useParams();
  const [module, setModule] = useState(null);
  const [assessments, setAssessments] = useState([]);
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const loadModule = async () => {
      try {
        setLoading(true);
        const [usersRes] = await Promise.all([
          userAPI.getAll().catch(() => ({ data: [] }))
        ]);
        setUsers(usersRes.data || []);

        // Try to get module by ID or code
        try {
          const moduleRes = await moduleAPI.getByCode(id);
          setModule(moduleRes.data);
          if (moduleRes.data?.assessments) {
            setAssessments(moduleRes.data.assessments);
          }
        } catch (e) {
          // If getByCode fails, try getting all and finding by ID
          const allModulesRes = await moduleAPI.getAll();
          const foundModule = (allModulesRes.data || []).find(
            m => (m.id || m.ID)?.toString() === id
          );
          if (foundModule) {
            setModule(foundModule);
            if (foundModule.assessments) {
              setAssessments(foundModule.assessments);
            }
          } else {
            setError('Module not found');
          }
        }

        // Try to load assessments separately if needed
        try {
          const assessmentRes = await assessmentAPI.getById(1);
          if (assessmentRes.data) {
            setAssessments(prev => [...prev, assessmentRes.data].filter((v, i, a) => 
              a.findIndex(t => (t.id || t.ID) === (v.id || v.ID)) === i
            ));
          }
        } catch (e) {
          // Assessment might not exist, that's okay
        }
      } catch (err) {
        setError('Failed to load module');
        console.error('Error loading module:', err);
      } finally {
        setLoading(false);
      }
    };

    loadModule();
  }, [id]);

  const getUserName = (userId) => {
    if (!userId) return 'N/A';
    const user = users.find(u => u.userID === userId || u.ID === userId);
    return user ? user.username : 'N/A';
  };

  const getProgressLabel = (progress) => {
    if (!progress) return 'N/A';
    return progress.replace(/_/g, ' ').toLowerCase()
      .replace(/\b\w/g, l => l.toUpperCase());
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

  if (error || !module) {
    return (
      <Layout>
        <div className="content">
          <h2>{error || 'Module not found'}</h2>
          <Link to="/modules">Back to Modules</Link>
        </div>
      </Layout>
    );
  }

  return (
    <Layout>
      <header className="header">
        <div className="h-title">{module.code || module.moduleCode || 'N/A'} • {module.title || 'Untitled Module'}</div>
        <div className="actions">
          <Link className="btn" to="/modules">Back</Link>
          <Link className="btn" to={`/modules/${id}/edit`}>Edit Module</Link>
          <Link className="btn primary" to={`/modules/${id}/assessments/new`}>Add Assessment</Link>
        </div>
      </header>

      <section className="grid two">
        <div className="card">
          <div className="label">Module Lead</div>
          <div className="mt-12">{getUserName(module.moduleLeaderID)}</div>
          <div className="sep"></div>
          <div className="label mt-12">Module Code</div>
          <div className="mt-12">{module.code || module.moduleCode || 'N/A'}</div>
          <div className="sep"></div>
          <div className="label mt-12">Module ID</div>
          <div className="mt-12">{module.id || module.ID || 'N/A'}</div>
        </div>
        <div className="card">
          <div className="h-title" style={{ fontSize: '18px' }}>Assessments</div>
          <table className="table mt-12">
            <thead>
              <tr>
                <th>Title</th>
                <th>Type</th>
                <th>Progress</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {assessments.length > 0 ? (
                assessments.map((a) => (
                  <tr key={a.id || a.ID}>
                    <td>{a.title || 'Untitled Assessment'}</td>
                    <td>{a.type || 'N/A'}</td>
                    <td>
                      <span className="badge">
                        {getProgressLabel(a.progress)}
                      </span>
                    </td>
                    <td>
                      <Link to={`/modules/${id}/assessments/${a.id || a.ID}`}>Open →</Link>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="4" style={{ textAlign: 'center', padding: '24px' }}>
                    <p className="sub">No assessments yet.</p>
                    <Link className="btn primary mt-12" to={`/modules/${id}/assessments/new`} style={{ display: 'inline-block' }}>
                      Add Assessment
                    </Link>
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </section>
    </Layout>
  );
}

export default ModuleDetail;

