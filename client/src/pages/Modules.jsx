import { Link } from 'react-router-dom';
import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import { moduleAPI, userAPI, assignedUserAPI } from '../services/api';
import { getCurrentUser, canManageModules, filterModulesByRole } from '../utils/permissions';

function Modules() {
  const [modules, setModules] = useState([]);
  const [filteredModules, setFilteredModules] = useState([]);
  const [users, setUsers] = useState([]);
  const [assignedUsers, setAssignedUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showArchived, setShowArchived] = useState(false);
  const [currentUser, setCurrentUser] = useState(null);

  useEffect(() => {
    const loadModules = async () => {
      try {
        setLoading(true);
        const user = getCurrentUser();
        setCurrentUser(user);

        const [modulesRes, usersRes, assignedRes] = await Promise.all([
          moduleAPI.getAll().catch((err) => {
            console.error('Error loading modules:', err);
            console.error('Error details:', err.message, err.stack);
            return { data: [] };
          }),
          userAPI.getAll().catch(() => ({ data: [] })),
          assignedUserAPI.getAll().catch(() => ({ data: [] }))
        ]);
        
        console.log('Modules API response:', modulesRes); // Debug log
        const allModules = modulesRes.data || [];
        console.log('Loaded modules:', allModules); // Debug log
        console.log('Number of modules:', allModules.length); // Debug log
        
        setModules(allModules);
        setUsers(usersRes.data || []);
        setAssignedUsers(assignedRes.data || []);

        // Filter modules by role
        const userId = user?.id || user?.userID;
        const username = user?.username;
        const currentView = user?.selectedUserType || user?.selectedRole;
        const filtered = filterModulesByRole(allModules, assignedRes.data || [], [], userId, username, currentView);
        
        // Filter archived modules
        const visibleModules = showArchived 
          ? filtered 
          : filtered.filter(m => !m.archived);
        setFilteredModules(visibleModules);
      } catch (err) {
        setError('Failed to load modules');
        console.error('Error loading modules:', err);
      } finally {
        setLoading(false);
      }
    };

    loadModules();
  }, [showArchived]);

  const canManage = canManageModules(
    assignedUsers, 
    currentUser?.id || currentUser?.userID, 
    currentUser?.username,
    currentUser?.selectedUserType || currentUser?.selectedRole
  );

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

  const handleArchiveToggle = (moduleId) => {
    // This will need backend support - for now just show UI
    alert('Archive functionality will be implemented when backend supports it');
  };

  const handleDelete = (module) => {
    if (window.confirm(`Are you sure you want to delete module "${module.code || module.moduleCode}"? This action cannot be undone.`)) {
      // This will need backend support - for now just show UI
      alert('Delete functionality will be implemented when backend supports it');
    }
  };

  return (
    <Layout>
      <header className="header">
        <div className="h-title">Modules</div>
        <div className="actions" style={{ display: 'flex', gap: '12px' }}>
          {canManage && (
            <Link className="btn primary" to="/modules/new">Add Module</Link>
          )}
          {canManage && (
            <Link className="btn" to="/modules/upload-csv">Upload CSV</Link>
          )}
          <label style={{ display: 'flex', alignItems: 'center', gap: '8px', cursor: 'pointer' }}>
            <input
              type="checkbox"
              checked={showArchived}
              onChange={(e) => setShowArchived(e.target.checked)}
            />
            <span style={{ fontSize: '14px' }}>Show Archived</span>
          </label>
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
              <th>Status</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {filteredModules.length > 0 ? (
              filteredModules.map((module) => (
                <tr key={module.id || module.ID}>
                  <td>{module.code ? `COM${module.code}` : module.moduleCode || 'N/A'}</td>
                  <td>{module.title || 'Untitled Module'}</td>
                  <td>{getUserName(module.leaderID || module.moduleLeaderID)}</td>
                  <td>{getAssessmentCount(module)}</td>
                  <td>
                    {module.archived ? (
                      <span className="badge" style={{ background: 'rgba(128, 128, 128, 0.2)', color: '#888' }}>
                        Archived
                      </span>
                    ) : (
                      <span className="badge" style={{ background: 'rgba(0, 217, 255, 0.2)', color: 'var(--brand)' }}>
                        Active
                      </span>
                    )}
                  </td>
                  <td>
                    <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
                      <Link to={`/modules/${module.id || module.ID}`}>Open →</Link>
                      {canManage && (
                        <>
                          <button
                            className="btn"
                            onClick={() => handleArchiveToggle(module.id || module.ID)}
                            style={{ padding: '4px 8px', fontSize: '12px' }}
                          >
                            {module.archived ? 'Unarchive' : 'Archive'}
                          </button>
                          <button
                            className="btn"
                            onClick={() => handleDelete(module)}
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
                  <p className="sub">No modules found.</p>
                  {canManage && (
                    <Link className="btn primary mt-12" to="/modules/new" style={{ display: 'inline-block' }}>
                      Create your first module
                    </Link>
                  )}
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

