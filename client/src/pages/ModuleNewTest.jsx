import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Layout from '../components/Layout';
import ModuleNew from './ModuleNew';
import { userAPI } from '../services/api';

/**
 * Developer test page for ModuleNew component
 *
 *  Hidden from production users — only accessible in development mode
 *  Usage:
 *    - Visit /test/module-new (only in development)
 *    - Use Quick Login or Set Mock User
 *    - Test ModuleNew component without navigating through full app
 */

function ModuleNewTest() {
  const navigate = useNavigate();
  const [currentUser, setCurrentUser] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    const storedUser = localStorage.getItem('currentUser');
    const token = localStorage.getItem('authToken');
    if (storedUser && token) {
      setCurrentUser(JSON.parse(storedUser));
    } else {
      setError('Not logged in. Use Quick Login or Set Mock User.');
    }
  }, []);

  const handleQuickLogin = async () => {
    try {
      const res = await userAPI.login('john', 'password'); // Replace with real test credentials if needed
      const user = res.data;

      localStorage.setItem('authToken', user.token);
      localStorage.setItem('currentUser', JSON.stringify({
        id: user.userID,
        username: user.username,
        email: user.email,
        userType: user.userType,
        selectedUserType: user.userType,
      }));
      setCurrentUser(user);
      setError('');
    } catch (err) {
      console.error('Quick login failed:', err);
      setError('Quick login failed: ' + (err.message || 'Unknown error'));
    }
  };

  const handleSetMockUser = () => {
    const mockUser = {
      id: 'dev-test-user',
      username: 'testuser',
      email: 'test@example.com',
      userType: 'TEACHING_SUPPORT',
      selectedUserType: 'TEACHING_SUPPORT',
    };
    localStorage.setItem('authToken', 'mock-token');
    localStorage.setItem('currentUser', JSON.stringify(mockUser));
    setCurrentUser(mockUser);
    setError('');
  };

  const handleLogout = () => {
    localStorage.removeItem('authToken');
    localStorage.removeItem('currentUser');
    setCurrentUser(null);
    setError('Not logged in. Use Quick Login or Set Mock User.');
  };

  // Restrict page to development mode only
  if (process.env.NODE_ENV !== 'development') {
    return (
      <div style={{ padding: '40px', fontFamily: 'sans-serif' }}>
        <h2> This test page is only available in development mode.</h2>
      </div>
    );
  }

  return (
    <Layout>
      <header className="header">
        <div className="h-title">Module Creation Test Page (DEV ONLY)</div>
        <div className="actions">
          <button className="btn" onClick={() => navigate('/dashboard')}>Back to Dashboard</button>
        </div>
      </header>

      <section className="card mt-24">
        <h3>Test Environment Info</h3>
        <div><strong>Current User:</strong> {currentUser ? `${currentUser.username} (${currentUser.email})` : 'Not logged in'}</div>
        <div><strong>Backend:</strong> http://localhost:8080</div>
        <div><strong>Module API Endpoint:</strong> POST /api/module/create</div>

        {error && (
          <div style={{
            marginTop: '16px',
            background: 'rgba(255, 51, 102, 0.1)',
            border: '1px solid var(--bad)',
            color: 'var(--bad)',
            padding: '12px',
            borderRadius: '4px'
          }}>
            {error}
          </div>
        )}

        {!currentUser && (
          <div className="mt-16" style={{ display: 'flex', gap: '10px', flexWrap: 'wrap' }}>
            <button className="btn primary" onClick={handleQuickLogin}>Quick Login (john/password)</button>
            <button className="btn" onClick={handleSetMockUser}>Set Mock User</button>
            <button className="btn" onClick={() => navigate('/login')}>Go to Login Page</button>
          </div>
        )}

        {currentUser && (
          <div className="mt-16">
            <button className="btn" onClick={handleLogout}>Log Out</button>
          </div>
        )}
      </section>

      <section className="card mt-24">
        <h3>ModuleNew Component</h3>
        <ModuleNew />
      </section>
    </Layout>
  );
}

export default ModuleNewTest;
