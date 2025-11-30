import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { userAPI, assignedUserAPI } from '../services/api';

function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [showRoleSelection, setShowRoleSelection] = useState(false);
  const [userRoles, setUserRoles] = useState([]);
  const [selectedRole, setSelectedRole] = useState('');
  const [userData, setUserData] = useState(null);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      // Validate username and password with backend
      const response = await userAPI.login(username, password);
      const user = response.data;
      
      // Fetch user's roles
      try {
        const rolesResponse = await assignedUserAPI.getUserRoles(username);
        const roles = rolesResponse.data || [];
        
        if (roles.length === 0) {
          setError('You do not have any roles assigned. Please contact an administrator.');
          setLoading(false);
          return;
        }
        
        // Store user data temporarily
        setUserData({
          id: user.userID,
          username: user.username,
          email: user.email,
          userType: user.userType
        });
        
        // If user has only one role, automatically proceed
        if (roles.length === 1) {
          localStorage.setItem('currentUser', JSON.stringify({
            id: user.userID,
            username: user.username,
            email: user.email,
            userType: user.userType,
            selectedRole: roles[0]
          }));
          navigate('/dashboard');
        } else {
          // Show role selection for multiple roles
          setUserRoles(roles);
          setShowRoleSelection(true);
        }
      } catch (roleError) {
        setError('Failed to fetch user roles. Please try again.');
        console.error('Role fetch error:', roleError);
      }
    } catch (err) {
      setError(err.message || 'Invalid username or password');
      console.error('Login error:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleRoleSelection = (e) => {
    e.preventDefault();
    setError('');

    if (!selectedRole) {
      setError('Please select a role to continue');
      return;
    }

    // Validate that the selected role is in the user's roles list
    if (!userRoles.includes(selectedRole)) {
      setError('Invalid role selected. Please select a valid role.');
      return;
    }

    // Store user info with selected role in localStorage
    localStorage.setItem('currentUser', JSON.stringify({
      ...userData,
      selectedRole: selectedRole
    }));
    
    navigate('/dashboard');
  };

  return (
    <main className="center">
      <section className="card login">
        <div className="brand" style={{ marginBottom: '10px' }}>
          <div className="logo"></div>
          <div>Assessment Tool</div>
        </div>
        
        {!showRoleSelection ? (
          <>
            <h2>Welcome back</h2>
            <p className="sub">Sign in to manage modules and assessments.</p>
            <form className="form" onSubmit={handleSubmit}>
              <div className="field">
                <label className="label" htmlFor="username">Username</label>
                <input
                  className="input"
                  id="username"
                  name="username"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  required
                />
              </div>
              <div className="field">
                <label className="label" htmlFor="password">Password</label>
                <input
                  className="input"
                  id="password"
                  name="password"
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
              </div>
              <button className="btn primary" type="submit" disabled={loading}>
                {loading ? 'Signing in...' : 'Sign in'}
              </button>
              {error && <p className="sub mt-12" style={{ color: 'red' }}>{error}</p>}
            </form>
            <p className="sub mt-18">Use seeded users: john, mary, kofi, sakura, musa</p>
          </>
        ) : (
          <>
            <h2>Select Your Role</h2>
            <p className="sub">You have multiple roles. Please select which role you want to use for this session.</p>
            <form className="form" onSubmit={handleRoleSelection}>
              <div className="field">
                <label className="label" htmlFor="role">Role</label>
                <select
                  className="input"
                  id="role"
                  name="role"
                  value={selectedRole}
                  onChange={(e) => setSelectedRole(e.target.value)}
                  required
                >
                  <option value="">-- Select a role --</option>
                  {userRoles.map((role, index) => (
                    <option key={index} value={role}>
                      {role.replace(/_/g, ' ')}
                    </option>
                  ))}
                </select>
              </div>
              <div style={{ display: 'flex', gap: '10px' }}>
                <button className="btn primary" type="submit" disabled={loading}>
                  Continue
                </button>
                <button
                  className="btn"
                  type="button"
                  onClick={() => {
                    setShowRoleSelection(false);
                    setSelectedRole('');
                    setUserRoles([]);
                    setUserData(null);
                    setError('');
                  }}
                >
                  Back
                </button>
              </div>
              {error && <p className="sub mt-12" style={{ color: 'red' }}>{error}</p>}
            </form>
          </>
        )}
      </section>
    </main>
  );
}

export default Login;

