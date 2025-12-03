import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { userAPI } from '../services/api';

function Login() {
  const [usernameOrEmail, setUsernameOrEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [showUserTypeSelection, setShowUserTypeSelection] = useState(false);
  const [availableUserTypes, setAvailableUserTypes] = useState([]);
  const [selectedUserType, setSelectedUserType] = useState('');
  const [userData, setUserData] = useState(null);
  const navigate = useNavigate();

  // Determine available user types based on primary userType
  const getAvailableUserTypes = (primaryUserType) => {
    const types = [];
    
    switch(primaryUserType) {
      case 'ROLE_EXAMS_OFFICER':
        // Exams Officer can choose: Academic View or Exam Officer View (Teaching Support Team)
        types.push('ROLE_ACADEMIC');
        types.push('ROLE_EXAMS_OFFICER'); // This will be displayed as "Teaching Support Team"
        break;
      case 'ROLE_ACADEMIC':
        types.push('ROLE_ACADEMIC');
        break;
      case 'ROLE_TEACHING_SUPPORT':
        types.push('ROLE_TEACHING_SUPPORT');
        break;
      case 'ROLE_EXTERNAL_EXAMINER':
        types.push('ROLE_EXTERNAL_EXAMINER');
        break;
      default:
        types.push(primaryUserType);
    }
    
    return types;
  };

  // Format user type for display
  const formatUserType = (userType) => {
    // Special case: Exams Officer view should be displayed as "Exam Officer View (Teaching Support Team)"
    if (userType === 'ROLE_EXAMS_OFFICER') {
      return 'Exam Officer View (Teaching Support Team)';
    }
    // Special case: Academic should be labeled as "Academic View"
    if (userType === 'ROLE_ACADEMIC') {
      return 'Academic View';
    }
    return userType
      .replace('ROLE_', '')
      .replace(/_/g, ' ')
      .toLowerCase()
      .replace(/\b\w/g, l => l.toUpperCase());
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      // Validate username/email and password with backend
      const response = await userAPI.login(usernameOrEmail, password);
      const user = response.data;
      
      // Store user data temporarily
      setUserData({
        id: user.userID,
        username: user.username,
        email: user.email,
        userType: user.userType
      });
      
      // Determine available user types based on primary userType
      const availableTypes = getAvailableUserTypes(user.userType);
      
      // If user has only one available type, automatically proceed
      if (availableTypes.length === 1) {
        localStorage.setItem('currentUser', JSON.stringify({
          id: user.userID,
          username: user.username,
          email: user.email,
          primaryUserType: user.userType,
          selectedUserType: availableTypes[0]
        }));
        navigate('/dashboard');
      } else {
        // Show user type selection for multiple available types
        setAvailableUserTypes(availableTypes);
        setShowUserTypeSelection(true);
      }
    } catch (err) {
      setError(err.message || 'Invalid username/email or password');
      console.error('Login error:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleUserTypeSelection = (e) => {
    e.preventDefault();
    setError('');

    if (!selectedUserType) {
      setError('Please select a user type to continue');
      return;
    }

    // Validate that the selected user type is available
    if (!availableUserTypes.includes(selectedUserType)) {
      setError('Invalid user type selected. Please select a valid user type.');
      return;
    }

    // Store user info with selected user type in localStorage
    localStorage.setItem('currentUser', JSON.stringify({
      ...userData,
      primaryUserType: userData.userType,
      selectedUserType: selectedUserType
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
        
        {!showUserTypeSelection ? (
          <>
            <h2>Welcome back</h2>
            <p className="sub">Sign in to manage modules and assessments.</p>
            <form className="form" onSubmit={handleSubmit}>
              <div className="field">
                <label className="label" htmlFor="usernameOrEmail">Username or Email</label>
                <input
                  className="input"
                  id="usernameOrEmail"
                  name="usernameOrEmail"
                  type="text"
                  value={usernameOrEmail}
                  onChange={(e) => setUsernameOrEmail(e.target.value)}
                  placeholder="Enter username or email"
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
              {error && <p className="sub mt-12" style={{ color: 'var(--bad)', textShadow: '0 0 10px rgba(255, 51, 102, 0.5)' }}>{error}</p>}
            </form>
            <p className="sub mt-18">Use seeded users: john, mary, kofi, sakura, musa<br />Or use their emails: john@example.com, mary@example.com, etc.</p>
          </>
        ) : (
          <>
            <h2>Select Your User Type</h2>
            <p className="sub">You have access to multiple user types. Please select which user type you want to use for this session.</p>
            <form className="form" onSubmit={handleUserTypeSelection}>
              <div className="field">
                <label className="label" htmlFor="userType">User Type</label>
                <select
                  className="input"
                  id="userType"
                  name="userType"
                  value={selectedUserType}
                  onChange={(e) => setSelectedUserType(e.target.value)}
                  required
                >
                  <option value="">-- Select a user type --</option>
                  {availableUserTypes.map((type, index) => (
                    <option key={index} value={type}>
                      {formatUserType(type)}
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
                    setShowUserTypeSelection(false);
                    setSelectedUserType('');
                    setAvailableUserTypes([]);
                    setUserData(null);
                    setError('');
                  }}
                >
                  Back
                </button>
              </div>
              {error && <p className="sub mt-12" style={{ color: 'var(--bad)', textShadow: '0 0 10px rgba(255, 51, 102, 0.5)' }}>{error}</p>}
            </form>
          </>
        )}
      </section>
    </main>
  );
}

export default Login;