import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Layout from '../../components/Layout';
import ModuleCSVUpload from '../ModuleCSVUpload'; // assumes your actual uploader is here
import { userAPI } from '../../services/api';

/**
 * Developer test page for ModuleCSVUpload component
 *
 * Usage:
 * 1. Run backend on http://localhost:8080
 * 2. Navigate to /test/module-upload
 * 3. Use quick login or set a mock user if not authenticated
 */
function ModuleCSVUploadTest() {
  const navigate = useNavigate();
  const [currentUser, setCurrentUser] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    const storedUser = localStorage.getItem('currentUser');
    const token = localStorage.getItem('authToken');

    if (storedUser && token) {
      setCurrentUser(JSON.parse(storedUser));
    } else {
      setError('Not logged in. Use quick login or mock user.');
    }
  }, []);

  const handleQuickLogin = async () => {
    try {
      const res = await userAPI.login('john', 'password');
      const user = res.data;

      localStorage.setItem('authToken', res.data.token);
      localStorage.setItem('currentUser', JSON.stringify({
        id: user.userID,
        username: user.username,
        email: user.email,
        userType: user.userType
      }));
      setCurrentUser(user);
      setError('');
    } catch (err) {
      setError('Quick login failed: ' + (err.message || 'Invalid credentials'));
    }
  };

  const handleSetMockUser = () => {
    const mockUser = {
      id: 'mock-id',
      username: 'testuser',
      email: 'test@example.com',
      userType: 'ROLE_ADMIN'
    };
    localStorage.setItem('authToken', 'mock-token');
    localStorage.setItem('currentUser', JSON.stringify(mockUser));
    setCurrentUser(mockUser);
    setError('');
  };

  const handleClearSession = () => {
    localStorage.removeItem('authToken');
    localStorage.removeItem('currentUser');
    setCurrentUser(null);
    setError('Not logged in. Use quick login or mock user.');
  };

  return (
    <Layout>
      <header className="header">
        <div className="h-title">CSV Upload Test Page</div>
        <div className="actions">
          <button className="btn" onClick={() => navigate('/dashboard')}>Back to Dashboard</button>
        </div>
      </header>

      <section className="card mt-24">
        <h3 style={{ marginBottom: '16px' }}>Test Environment Info</h3>
        <div><strong>Current User:</strong> {currentUser ? `${currentUser.username} (${currentUser.email})` : 'Not Logged In'}</div>
        <div className="mt-8"><strong>Backend:</strong> http://localhost:8080</div>
        <div className="mt-8"><strong>Upload Path:</strong> /api/module/csv</div>

        {error && (
          <div style={{
            background: 'rgba(255, 51, 102, 0.1)',
            border: '1px solid var(--bad)',
            color: 'var(--bad)',
            padding: '12px',
            borderRadius: '4px',
            marginTop: '16px'
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
            <button className="btn" onClick={handleClearSession}>Log Out</button>
          </div>
        )}
      </section>

      <section className="card mt-24">
        <h3 style={{ marginBottom: '16px' }}>Upload Test</h3>
        <ModuleCSVUpload />
      </section>
    </Layout>
  );
}

export default ModuleCSVUploadTest;
