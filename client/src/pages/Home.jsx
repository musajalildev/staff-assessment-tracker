import { Link } from 'react-router-dom';

function Home() {
  return (
    <main className="center">
      <section className="card login" style={{ maxWidth: '600px', textAlign: 'center' }}>
        <div className="brand" style={{ marginBottom: '30px', justifyContent: 'center' }}>
          <div className="logo"></div>
          <div style={{ fontSize: '32px' }}>Assessment Tool</div>
        </div>
        
        <h1 style={{ 
          fontSize: '48px', 
          fontWeight: '800', 
          background: 'linear-gradient(135deg, var(--brand), var(--brand-2))',
          WebkitBackgroundClip: 'text',
          WebkitTextFillColor: 'transparent',
          marginBottom: '20px',
          textShadow: '0 0 30px var(--brand-glow)'
        }}>
          Welcome
        </h1>
        
        <p className="sub" style={{ fontSize: '18px', marginBottom: '30px', lineHeight: '1.6' }}>
          Assessment tracking system for the School of Computer Science.<br />
          Manage modules, assessments, and track progress seamlessly.
        </p>

        <div style={{ 
          display: 'flex', 
          flexDirection: 'column', 
          gap: '16px', 
          marginTop: '40px' 
        }}>
          <Link 
            to="/login" 
            className="btn primary" 
            style={{ 
              padding: '16px 32px', 
              fontSize: '16px',
              fontWeight: '600',
              textDecoration: 'none',
              display: 'inline-block',
              width: '100%'
            }}
          >
            Sign In
          </Link>
        </div>

        <div style={{ 
          marginTop: '40px', 
          padding: '20px', 
          background: 'rgba(0, 217, 255, 0.05)', 
          borderRadius: '12px',
          border: '1px solid var(--border)'
        }}>
          <p className="sub" style={{ fontSize: '14px', color: 'var(--muted)' }}>
            <strong>Getting Started:</strong><br />
            Use seeded accounts: john, mary, kofi, sakura, musa<br />
            Or use their emails: john@example.com, mary@example.com, etc.
          </p>
        </div>
      </section>
    </main>
  );
}

export default Home;

