import { Link, useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import { moduleAPI, assessmentAPI, userAPI, assignedUserAPI } from '../services/api';
import { filterModulesByRole } from '../utils/permissions';

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

        // Skip role validation for exams officers - they switch between views (not assessment roles)
        // selectedRole/selectedUserType for exams officers is a VIEW (Academic vs Exam Officer View), not an assessment role
        const isExamsOfficer = user?.primaryUserType === 'ROLE_EXAMS_OFFICER' || 
                               user?.userType === 'ROLE_EXAMS_OFFICER';
        
        // Only validate selectedRole for non-exams-officers, and only if it's set
        // Note: getUserRoles returns AssessmentRole enum values (SETTER, CHECKER, etc.), not user types
        if (user.selectedRole && !isExamsOfficer) {
          try {
            const rolesResponse = await assignedUserAPI.getUserRoles(user.username);
            const validRoles = rolesResponse.data || [];

            // Check if the selected role is valid (assessment roles only)
            // For exams officers, selectedRole is 'ACADEMIC' or 'EXAM_OFFICER' (view indicators), not assessment roles
            if (!validRoles.includes(user.selectedRole)) {
              // Only error if it's actually an invalid assessment role
              // For exams officers, selectedRole represents a view, not an assessment role
              setRoleError('Invalid role selected. Your selected role is not assigned to your account.');
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

        const allModules = modulesRes.data || [];
        const userId = user?.id || user?.userID;
        const username = user?.username;
        const currentView = user?.selectedUserType || user?.selectedRole;
        console.log("test3");
        console.log(assignedRes);
        // Filter modules by role
        const filteredModules = filterModulesByRole(allModules, assignedRes.data || [], [], userId, username, currentView);

        setModules(filteredModules);
        setUsers(usersRes.data || []);
        setUserRoles(assignedRes.data || []);
        console.log(assignedRes);

        // Try to get assessments (if any exist)
        try {
          const assessmentRes = await assessmentAPI.getAll();
          setAssessments(assessmentRes.data);
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
  }, [navigate]); // Note: When view switches, Layout.jsx does window.location.reload() so this will re-run

  const getCurrentUserRoles = () => {
    if (!currentUser) return [];
    console.log("Test2")
    console.log(userRoles);
    console.log(userRoles
      .map(au => au.role));
    console.log(userRoles
      .filter(au => au.user?.userID === currentUser.id || au.user?.username === currentUser.username)
      .map(au => au.role));
    console.log(userRoles
      .filter(au => au.user?.userID === currentUser.id || au.user?.username === currentUser.username));
    return userRoles
      .filter(au => au.user?.userID === currentUser.id || au.user?.username === currentUser.username)
      .map(au => au.role);
  };

  const userRolesList = getCurrentUserRoles();

  assessments.slice(0, 3).map((assessment) => (console.log(assessment.id)));
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
        <div className="card" style={{ background: 'rgba(255, 51, 102, 0.1)', borderColor: 'var(--bad)', boxShadow: '0 0 20px rgba(255, 51, 102, 0.3)' }}>
          <h2 style={{ color: 'var(--bad)' }}>Invalid Role</h2>
          <p style={{ color: 'var(--text)' }}>{roleError}</p>
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
        <div className="card" style={{ marginBottom: '20px', borderColor: 'var(--brand)', boxShadow: '0 0 20px var(--brand-glow)' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
            <span className="label">Current Role:</span>
            <span className="pill" style={{ fontSize: '14px', fontWeight: 'bold', background: 'rgba(0, 217, 255, 0.15)', borderColor: 'var(--brand)', color: 'var(--brand)' }}>
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
                    background: 'rgba(0, 217, 255, 0.2)',
                    borderColor: 'var(--brand)',
                    color: 'var(--brand)',
                    fontWeight: 'bold',
                    boxShadow: '0 0 10px var(--brand-glow)'
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
          <div className="h-title" style={{ fontSize: '18px' }}>
            {currentUser?.selectedRole === 'EXTERNAL_EXAMINER' ? 'Assigned Modules' : 'Modules'}
          </div>
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
                modules.filter(m => !m.archived).slice(0, 5).map((module) => (
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
                  <td colSpan="3" className="sub">No modules found.</td>
                </tr>
              )}
            </tbody>
          </table>
          {modules.length > 5 && (
            <div style={{ marginTop: '12px', textAlign: 'right' }}>
              <Link to="/modules" className="btn" style={{ fontSize: '14px', padding: '6px 12px' }}>
                View All →
              </Link>
            </div>
          )}
        </div>
        <div className="card">
          <div className="h-title" style={{ fontSize: '18px' }}>Recent Assessments</div>
          <div className="timeline mt-12">
            {assessments.length > 0 ? (
              assessments.slice(0, 3).map((assessment) => (
                <div key={assessment.id || assessment.ID} className="step active">
                  <div className="dot"></div>
                  <div>
                    <div className="title"><Link to={`/modules/${assessment.module || 1}/assessments/${assessment.id || assessment.ID}`}>{assessment.title || 'Untitled Assessment'}</Link></div>
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

