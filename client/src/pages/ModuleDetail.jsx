import { Link, useParams } from 'react-router-dom';
import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import { moduleAPI, userAPI, assessmentAPI, assignedUserAPI } from '../services/api';
import { getCurrentUser, canManageModules } from '../utils/permissions';

function ModuleDetail() {
  const { id } = useParams();
  const [module, setModule] = useState(null);
  const [assessments, setAssessments] = useState([]);
  const [users, setUsers] = useState([]);
  const [assignedUsers, setAssignedUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [currentUser, setCurrentUser] = useState(null);

  useEffect(() => {
    const loadModule = async () => {
      try {
        setLoading(true);
        setError(''); // Clear any previous errors
        const user = getCurrentUser();
        setCurrentUser(user);
        
        const [usersRes, assignedRes] = await Promise.all([
          userAPI.getAll().catch(() => ({ data: [] })),
          assignedUserAPI.getAll().catch(() => ({ data: [] }))
        ]);
        setUsers(usersRes.data || []);
        setAssignedUsers(assignedRes.data || []);

        // Try to get module by ID or code
        try {
          // First try to get all modules and find by ID
          const allModulesRes = await moduleAPI.getAll();
          console.log('All modules response:', allModulesRes);
          const foundModule = (allModulesRes.data || []).find(
            m => {
              const moduleId = (m.id || m.ID)?.toString();
              const moduleCode = (m.code || m.moduleCode)?.toString();
              return moduleId === id || moduleCode === id || moduleCode === `COM${id}` || moduleId === id;
            }
          );
          
          if (foundModule) {
            console.log('Found module:', foundModule);
            console.log('Module assessments:', foundModule.assessments);
            console.log('Assessments type:', typeof foundModule.assessments);
            console.log('Assessments length:', foundModule.assessments?.length);
            
            setModule(foundModule);
            
            // Always set assessments, even if empty array
            // Check for assessments in multiple possible locations
            const moduleAssessments = foundModule.assessments || foundModule.assessmentList || [];
            console.log('Setting assessments:', moduleAssessments);
            console.log('Full module object keys:', Object.keys(foundModule));
            setAssessments(Array.isArray(moduleAssessments) ? moduleAssessments : []);
            
            if (moduleAssessments.length === 0) {
              console.warn('No assessments found in module data. Module ID:', foundModule.id || foundModule.ID);
              console.warn('Module object:', JSON.stringify(foundModule, null, 2));
            }
          } else {
            console.warn('Module not found in list, trying getByCode with id:', id);
            // Try getByCode as fallback
            try {
              const moduleRes = await moduleAPI.getByCode(parseInt(id));
              if (moduleRes.data) {
                console.log('Found module via getByCode:', moduleRes.data);
                setModule(moduleRes.data);
                const assessments = moduleRes.data?.assessments || [];
                setAssessments(assessments);
                console.log('Assessments from getByCode:', assessments);
              } else {
                setError('Module not found');
              }
            } catch (e2) {
              console.error('Error in getByCode fallback:', e2);
              setError('Module not found');
            }
          }
        } catch (e) {
          console.error('Error loading module:', e);
          setError('Failed to load module');
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
        <div className="h-title">{module.code ? `COM${module.code}` : module.moduleCode || 'N/A'} • {module.title || 'Untitled Module'}</div>
        <div className="actions">
          <Link className="btn" to="/modules">Back</Link>
          {canManageModules(assignedUsers, currentUser?.id || currentUser?.userID, currentUser?.username, currentUser?.selectedUserType || currentUser?.selectedRole) && (
            <>
              <Link className="btn" to={`/modules/${id}/edit`}>Edit Module</Link>
              <Link className="btn primary" to={`/modules/${id}/assessments/new`}>Add Assessment</Link>
            </>
          )}
        </div>
      </header>

      <section className="grid two">
        <div className="card">
          <div className="label">Module Lead</div>
          <div className="mt-12">{getUserName(module.leaderID || module.moduleLeaderID)}</div>
          <div className="sep"></div>
          <div className="label mt-12">Moderator</div>
          <div className="mt-12">
            {module.moderatorID ? getUserName(module.moderatorID) : 'Not assigned'}
          </div>
          <div className="sep"></div>
          <div className="label mt-12">Module Staff</div>
          <div className="mt-12">
            {module.staffIDs && module.staffIDs.length > 0 ? (
              <div style={{ display: 'flex', flexDirection: 'column', gap: '4px' }}>
                {module.staffIDs.map((staffId, idx) => (
                  <span key={idx}>{getUserName(staffId)}</span>
                ))}
              </div>
            ) : (
              'No additional staff assigned'
            )}
          </div>
          <div className="sep"></div>
          <div className="label mt-12">Module Code</div>
          <div className="mt-12">{module.code ? `COM${module.code}` : module.moduleCode || 'N/A'}</div>
          <div className="sep"></div>
          <div className="label mt-12">Status</div>
          <div className="mt-12">
            {module.archived ? (
              <span className="badge" style={{ background: 'rgba(128, 128, 128, 0.2)', color: '#888' }}>
                Archived
              </span>
            ) : (
              <span className="badge" style={{ background: 'rgba(0, 217, 255, 0.2)', color: 'var(--brand)' }}>
                Active
              </span>
            )}
          </div>
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
                    <td>{a.assessmentType || a.type || 'N/A'}</td>
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

