import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    // TODO: Implement actual authentication
    // For now, just navigate to dashboard
    navigate('/dashboard');
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
          <button className="btn primary" type="submit">Sign in</button>
        </form>
        <p className="sub mt-18">Demo only — no authentication wired yet.</p>
      </section>
    </main>
  );
}

export default Login;

