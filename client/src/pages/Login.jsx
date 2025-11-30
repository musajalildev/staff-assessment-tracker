import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { userAPI } from '../services/api';

function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      // Validate username and password with backend
      const response = await userAPI.login(username, password);
      const user = response.data;
      
      // Store user info in localStorage for session
      localStorage.setItem('currentUser', JSON.stringify({
        id: user.userID,
        username: user.username,
        email: user.email,
        userType: user.userType
      }));
      
      navigate('/dashboard');
    } catch (err) {
      setError(err.message || 'Invalid username or password');
      console.error('Login error:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="center">
      <section className="card login">
        <div className="brand" style={{ marginBottom: '10px' }}>
          <div className="logo"></div>
          <div>Assessment Tool</div>
        </div>
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
      </section>
    </main>
  );
}

export default Login;

