import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Layout from '../../components/Layout';
import FeedbackSection from '../../components/FeedbackSection';
import { assessmentAPI, userAPI } from '../../services/api';

/**
 * Standalone test page for FeedbackSection component
 * 
 * Usage:
 * 1. Make sure backend is running on http://localhost:8080
 * 2. Navigate to /test/feedback/:assessmentId (e.g., /test/feedback/1)
 * 3. Make sure you're logged in (or set localStorage manually)
 */
function FeedbackTest() {
  const { assessmentId } = useParams();
  const navigate = useNavigate();
  const [currentUser, setCurrentUser] = useState(null);
  const [assessment, setAssessment] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [on, setOn] = useState(true);

  const toggle = () => setOn(!on);
  useEffect(() => {
    // Check if user is logged in
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      setCurrentUser(JSON.parse(storedUser));
    } else {
      // If not logged in, try to login with a test user
      // You can modify these credentials or login manually first
      setError('Not logged in. Please login first or set localStorage manually.');
    }
    setLoading(false);
  }, []);

  useEffect(() => {
    const loadAssessment = async () => {
      try {
        setLoading(true);
        const assessmentIdToUse = assessmentId || 1; // Fallback to 1 if not provided

        const [assessmentRes, usersRes] = await Promise.all([
          assessmentAPI.getById(assessmentIdToUse),
          userAPI.getAll().catch(() => ({ data: [] }))
        ]);

        setAssessment(assessmentRes.data);
      } catch (err) {
        setError('Failed to load assessment');
        console.error('Error loading assessment:', err);
      } finally {
        setLoading(false);
      }
    };

    loadAssessment();
  }, [assessmentId]);

  const progressAssessment = async () => {
    if (!assessment) return;
    if (assessment.progress != "CHECKED") {
      return;
    }


    let nextProgress = "";
    if (on) {
      nextProgress = "NEEDS_CHANGES";
    } else {
      nextProgress = "TEST_TAKING_PLACE"
    }



    const updatedAssessment = {
      ...assessment,
      progress: nextProgress
    };

    try {
      const result = await assessmentAPI.update(assessment.id || assessment.ID, updatedAssessment);
      setAssessment(result.data);
      navigate("/dashboard")
    } catch (err) {
      console.error('Error updating assessment:', err);
      alert('Failed to update assessment progress');
    }
  };

  const getUserName = (user) => {
    if (!user) return 'N/A';
    if (typeof user === 'string') return user;
    return user.username || user.email || 'N/A';
  };

  if (loading) {
    return (
      <Layout>
        <div className="card">
          <p>Loading assessment...</p>
        </div>
      </Layout>
    );
  }

  if (error || !assessment) {
    return (
      <Layout>
        <div className="content">
          <h2>{error || 'Assessment not found'}</h2>
          <Link to={`/modules/${moduleId}`}>Back to Module</Link>
        </div>
      </Layout>
    );
  }

  const handleQuickLogin = async () => {
    try {
      // Try to login with a test user (modify credentials as needed)
      const response = await userAPI.login('john', 'password');
      const user = response.data;

      const userData = {
        id: user.userID,
        username: user.username,
        email: user.email,
        userType: user.userType
      };

      localStorage.setItem('currentUser', JSON.stringify(userData));
      setCurrentUser(userData);
      setError('');
    } catch (err) {
      setError('Quick login failed: ' + (err.message || 'Invalid credentials'));
    }
  };

  const handleSetMockUser = () => {
    // Set a mock user for testing
    const mockUser = {
      id: '123e4567-e89b-12d3-a456-426614174000', // UUID format
      username: 'testuser',
      email: 'test@example.com',
      userType: 'ROLE_ACADEMIC'
    };
    localStorage.setItem('currentUser', JSON.stringify(mockUser));
    setCurrentUser(mockUser);
    setError('');
  };

  if (loading) {
    return (
      <Layout>
        <div className="card">
          <p>Loading test page...</p>
        </div>
      </Layout>
    );
  }

  return (
    <Layout>
      <header className="header">
        <div className="h-title">Feedback Section Test Page</div>
        <div className="actions">
          <button className="btn" onClick={() => navigate('/dashboard')}>
            Back to Dashboard
          </button>
        </div>
      </header>

      <section className="card mt-24">
        <div className="h-title" style={{ fontSize: '18px' }}>Test Setup</div>

        {error && (
          <div className="card" style={{
            background: 'rgba(255, 51, 102, 0.1)',
            color: 'var(--bad)',
            marginTop: '16px',
            borderColor: 'var(--bad)'
          }}>
            {error}
          </div>
        )}

        <div className="mt-12">
          <div><strong>Assessment ID:</strong> {assessmentId || 'Not set'}</div>
          <div className="mt-12">
            <strong>Current User:</strong>{' '}
            {currentUser ? (
              <span>{currentUser.username} ({currentUser.id})</span>
            ) : (
              <span style={{ color: 'var(--bad)' }}>Not logged in</span>
            )}
          </div>
          <div className="mt-12">
            <strong>Backend URL:</strong> http://localhost:8080
          </div>
        </div>

        {!currentUser && (
          <div className="mt-24" style={{ display: 'flex', gap: '10px', flexWrap: 'wrap' }}>
            <button className="btn primary" onClick={handleQuickLogin}>
              Quick Login (john/password)
            </button>
            <button className="btn" onClick={handleSetMockUser}>
              Set Mock User (for testing without backend)
            </button>
            <button className="btn" onClick={() => navigate('/login')}>
              Go to Login Page
            </button>
          </div>
        )}

        <div className="sep mt-24"></div>

        <div className="mt-24">
          <div className="h-title" style={{ fontSize: '16px' }}>Instructions</div>
          <ol style={{ marginTop: '12px', paddingLeft: '20px', lineHeight: '1.8' }}>
            <li>Make sure the backend server is running on port 8080</li>
            <li>Ensure you're logged in (use Quick Login or go to Login page)</li>
            <li>The assessment ID is taken from the URL: <code>/test/feedback/:assessmentId</code></li>
            <li>Try creating, viewing, and deleting feedback</li>
            <li>Check browser console for any API errors</li>
          </ol>
        </div>
      </section>
      <button onClick={() => setOn(!on)} >{on ? "Needs Changes" : "No Changes Needed"}</button>

      {assessmentId && (
        <div className="mt-24">
          <FeedbackSection
            assessment={assessment}
            onFeedbackAdded={() => {
              progressAssessment()
              console.log('Feedback added callback triggered');
            }}
          />
        </div>
      )}

      {!assessmentId && (
        <section className="card mt-24">
          <div className="h-title" style={{ fontSize: '18px' }}>No Assessment ID</div>
          <p className="sub mt-12">
            Navigate to <code>/test/feedback/1</code> (or any assessment ID) to test the feedback section.
          </p>
        </section>
      )}
    </Layout>
  );
}

export default FeedbackTest;

