import { Link, useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import { moduleAPI, assessmentAPI, userAPI, assignedUserAPI } from '../services/api';

function Dashboard() {
  const [modules, setModules] = useState([]);
  const [assessments, setAssessments] = useState([]);
  const [users, setUsers] = useState([]);
  const [userRoles, setUserRoles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [currentUser, setCurrentUser] = useState(null);
  const [roleError, setRoleError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const loadData = async () => {
      try {
        const storedUser = localStorage.getItem('currentUser');
        if (!storedUser) {
          navigate('/login');
          return;
        }

        const user = JSON.parse(storedUser);
        setCurrentUser(user);

        // Validate selected role if it exists
        if (user.selectedRole) {
          try {
            const rolesResponse = await assignedUserAPI.getUserRoles(user.username);
            const validRoles = rolesResponse.data || [];
            
            // Check if the selected role is valid
            if (!validRoles.includes(user.selectedRole)) {
              setRoleError('Invalid role selected. Your selected role is not assigned to your account.');
              // Remove invalid role and redirect to login after a delay
              setTimeout(() => {
                localStorage.removeItem('currentUser');
                navigate('/login');
              }, 3000);
              return;
            }
          } catch (roleError) {
            console.error('Error validating role:', roleError);
            // If we can't validate, continue but log the error
          }
        }

        // Load all data
        const [modulesRes, usersRes, assignedRes] = await Promise.all([
          moduleAPI.getAll().catch(() => ({ data: [] })),
          userAPI.getAll().catch(() => ({ data: [] })),
          assignedUserAPI.getAll().catch(() => ({ data: [] }))
        ]);

        setModules(modulesRes.data || []);
        setUsers(usersRes.data || []);
        setUserRoles(assignedRes.data || []);

        // Try to get assessments (if any exist)
        try {
          const assessmentRes = await assessmentAPI.getById(1);
          setAssessments([assessmentRes.data]);
        } catch (e) {
          setAssessments([]);
        }
      } catch (error) {
        console.error('Error loading dashboard data:', error);
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, [navigate]);

  const getCurrentUserRoles = () => {
    if (!currentUser) return [];
    return userRoles
      .filter(au => au.user?.userID === currentUser.id || au.user?.username === currentUser.username)
      .map(au => au.role);
  };

  const userRolesList = getCurrentUserRoles();
  const assessmentsInProgress = assessments.filter(a => 
    a.progress && a.progress !== 'COMPLETE'
  ).length;

  if (loading) {
    return (
      <Layout>
        <div className="card">
          <p>Loading dashboard...</p>
        </div>
      </Layout>
    );
  }

  if (roleError) {
    return (
      <Layout>
        <div className="card" style={{ background: '#fee', color: '#c00', border: '1px solid #fcc' }}>
          <h2 style={{ color: '#c00' }}>Invalid Role</h2>
          <p>{roleError}</p>
          <p className="sub mt-12">Redirecting to login...</p>
        </div>
      </Layout>
    );
  }

  return (
    <Layout>
      <header className="header">
        <div className="h-title">Overview</div>
        <div className="actions">
          <Link className="btn" to="/modules">View all modules</Link>
        </div>
      </header>

      {currentUser?.selectedRole && (
        <div className="card" style={{ marginBottom: '16px', background: '#e8f4f8', border: '1px solid #b8d4e0' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <span className="label">Current Role:</span>
            <span className="pill" style={{ fontSize: '14px', fontWeight: 'bold' }}>
              {currentUser.selectedRole.replace(/_/g, ' ')}
            </span>
          </div>
        </div>
      )}

      <section className="grid three">
        <div className="card">
          <div className="label">Total Modules</div>
          <h2>{modules.length}</h2>
          <p className="sub mt-12">All faculties • sorted by code</p>
        </div>
        <div className="card">
          <div className="label">Assessments in Progress</div>
          <h2>{assessmentsInProgress}</h2>
          <p className="sub mt-12">Across Coursework, Tests, Exams</p>
        </div>
        <div className="card">
          <div className="label">Your Roles</div>
          <div className="mt-12">
            {userRolesList.length > 0 ? (
              userRolesList.map((role, i) => (
                <span 
                  key={i} 
                  className="pill" 
                  style={currentUser?.selectedRole === role ? { 
                    background: '#4a90e2', 
                    color: 'white',
                    fontWeight: 'bold'
                  } : {}}
                >
                  {role.replace(/_/g, ' ')}
                </span>
              ))
            ) : (
              <span className="sub">No roles assigned</span>
            )}
          </div>
        </div>
      </section>

      <section className="grid two mt-24">
        <div className="card">
          <div className="h-title" style={{ fontSize: '18px' }}>All Modules</div>
          <table className="table mt-12">
            <thead>
              <tr>
                <th>Code</th>
                <th>Title</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {modules.length > 0 ? (
                modules.map((module) => (
                  <tr key={module.id || module.ID}>
                    <td>{module.code || module.moduleCode}</td>
                    <td>{module.title || 'N/A'}</td>
                    <td>
                      <Link to={`/modules/${module.id || module.ID}`}>Open →</Link>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="3" className="sub">No modules found. Create one to get started.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
        <div className="card">
          <div className="h-title" style={{ fontSize: '18px' }}>Recent Assessments</div>
          <div className="timeline mt-12">
            {assessments.length > 0 ? (
              assessments.slice(0, 3).map((assessment) => (
                <div key={assessment.id || assessment.ID} className="step active">
                  <div className="dot"></div>
                  <div>
                    <div className="title">{assessment.title || 'Untitled Assessment'}</div>
                    <div className="meta">
                      {assessment.type || 'N/A'} • {assessment.progress || 'N/A'}
                    </div>
                  </div>
                </div>
              ))
            ) : (
              <div className="sub">No assessments yet</div>
            )}
          </div>
        </div>
      </section>
    </Layout>
  );
}

export default Dashboard;

