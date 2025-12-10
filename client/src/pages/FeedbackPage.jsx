import { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import Layout from '../components/Layout';
import { assessmentAPI, userAPI } from '../services/api';
import { feedbackAPI } from '../services/api';



function FeedbackPage() {
    const { assessmentId } = useParams();
    const [feedbackType, setFeedbackType] = useState("");
    const [feedbackList, setFeedbackList] = useState([])
    const [newFeedback, setNewFeedback] = useState('');;
    const navigate = useNavigate();
    const [showText, setShowText] = useState('');
    const [currentUser, setCurrentUser] = useState(null);
    const [assessment, setAssessment] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [submitting, setSubmitting] = useState(false);
    const [on, setOn] = useState(true);

    const toggle = () => setOn(!on);
    // Authenthication
    useEffect(() => {
        // Check If Logged In
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
    // Loads the assessment
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
        loadFeedback();

        //Check if assessment needs feedback And whether the user can give feedback
    }, [assessmentId]);
    const loadFeedback = async () => {
        setLoading(true);
        setError('');
        try {
            // Try to fetch feedback, but handle gracefully if endpoint doesn't exist yet
            const response = await feedbackAPI.getByAssessment(assessmentId);
            setFeedbackList(response.data || []);
        } catch (err) {
            // If endpoint doesn't exist, just show empty list
            if (err.message && !err.message.includes('404')) {
                setError('Failed to load feedback.');
                console.error('Error loading feedback:', err);
            }
            setFeedbackList([]);
        } finally {
            setLoading(false);
        }
    };

    const handleSubmitFeedback = async (e) => {
        if (!assessment) return;
        if (!(assessment.progress == "CHECKED" || assessment.progress == "EXAM_OFFICER_CHECKED" || assessment.progress == "EXTERNAL_EXAMINER_CHECK" || assessment.progress == "SETTER_FORMAL_RESPONSE")) {
            return;
        }
        e.preventDefault();
        if (!newFeedback.trim()) {
            setError('Feedback cannot be empty.');
            return;
        }
        if (!currentUser || !currentUser.id) {
            setError('User not logged in. Please log in to add feedback.');
            return;
        }

        setSubmitting(true);
        setError('');
        switch (assessment.progress) {
            case "CHECKED":
                setFeedbackType("CHECKER");
            case "EXAM_OFFICER_CHECKED":
                setFeedbackType("EXAM_OFFICER");
            case "EXTERNAL_EXAMINER_CHECK":
                setFeedbackType("EXTERNAL_EXAMINER");
            case "SETTER_FORMAL_RESPONSE":
                setFeedbackType("SETTER_RESPONSE");
        }

        const feedbackData = {
            feedback: newFeedback.trim(),
            assessmentID: parseInt(assessmentId),
            feedbackType: feedbackType
        };


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

        try {
            await feedbackAPI.create(assessmentId, feedbackData);
            setNewFeedback('');
            loadFeedback(); // Reload feedback list
            if (onFeedbackAdded) {
                onFeedbackAdded(); // Notify parent component if needed
            }
        } catch (err) {
            setError(err.message || 'Failed to add feedback.');
            console.error('Error adding feedback:', err);
        } finally {
            setSubmitting(false);
        }
    };

    const handleDeleteFeedback = async (feedbackId) => {
        if (!window.confirm('Are you sure you want to delete this feedback?')) {
            return;
        }

        try {
            await feedbackAPI.delete(feedbackId);
            loadFeedback(); // Reload feedback list
        } catch (err) {
            setError(err.message || 'Failed to delete feedback.');
            console.error('Error deleting feedback:', err);
        }
    };

    const formatDate = (dateString) => {
        if (!dateString) return 'Date unknown';
        try {
            const date = new Date(dateString);
            return date.toLocaleDateString('en-US', {
                year: 'numeric',
                month: 'short',
                day: 'numeric',
                hour: '2-digit',
                minute: '2-digit'
            });
        } catch {
            return dateString;
        }
    };

    useEffect(() => {
        if (!assessment) return;
        switch (assessment.progress) {
            case "CHECKED":
                setShowText("Checker Feedback");
                break;
            case "EXAM_OFFICER_CHECKED":
                setShowText("Exam Officer Feedback");
                break;
            case "EXTERNAL_EXAMINER_CHECK":
                setShowText("External Examiner Feedback");
                break;
            case "SETTER_FORMAL_RESPONSE":
                setShowText("Response To External Examiner");
                break;
        }
    })

    const getUserName = (user) => {
        if (!user) return 'N/A';
        if (typeof user === 'string') return user;
        return user.username || user.email || 'N/A';
    };

    if (loading) {
        return (
            <Layout>
                <div className="card">
                    <p>Loading Feedback Page...</p>
                </div>
            </Layout>
        );
    }

    if (error || !assessment) {
        return (
            <Layout>
                <div className="content">
                    <h2>{error || 'Assessment not found'}</h2>
                    <Link to={`/dashboard`}>Back to dashboard</Link>
                </div>
            </Layout>
        );
    }
    if (!(assessment.progress == "CHECKED" || assessment.progress == "EXAM_OFFICER_CHECKED" || assessment.progress == "EXTERNAL_EXAMINER_CHECK" || assessment.progress == "SETTER_FORMAL_RESPONSE")) {
        navigate("/modules/" + assessment.moduleId + "/assessments/" + assessment.ID)
        return;
    }

    return (
        <Layout>
            <header className="header">
                <div className="h-title">{showText}</div>
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
            {assessment.type == "CHECKED" || assessment.type == "EXAM_OFFICER_CHECKED" && (<button onClick={() => setOn(!on)} >{on ? "Needs Changes" : "No Changes Needed"}</button>)}

            {assessmentId && (
                <div className="mt-24">
                    <section className="card mt-24">
                        <div className="h-title" style={{ fontSize: '18px' }}>Feedback & History</div>

                        {error && (
                            <div className="card" style={{ background: 'rgba(255, 51, 102, 0.1)', color: 'var(--bad)', marginBottom: '16px', borderColor: 'var(--bad)' }}>
                                {error}
                            </div>
                        )}

                        <div className="feedback-timeline mt-12">
                            {feedbackList.length > 0 ? (
                                feedbackList.map((fb) => (
                                    <div key={fb.id || fb.ID} className="feedback-item" style={{
                                        padding: '16px',
                                        marginBottom: '16px',
                                        background: 'rgba(20, 25, 45, 0.3)',
                                        borderRadius: '12px',
                                        border: '1px solid var(--border)',
                                        position: 'relative'
                                    }}>
                                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start', marginBottom: '8px' }}>
                                            <div>
                                                <span style={{ color: 'var(--brand)', fontWeight: '600' }}>
                                                    {fb.authorUsername || fb.author?.username || 'Unknown User'}
                                                </span>
                                                {fb.createdDate && (
                                                    <span style={{ color: 'var(--muted)', fontSize: '0.9rem', marginLeft: '12px' }}>
                                                        {formatDate(fb.createdDate)}
                                                    </span>
                                                )}
                                            </div>
                                            {currentUser && (currentUser.id === fb.authorID || currentUser.id === fb.author?.userID) && (
                                                <button
                                                    className="btn"
                                                    onClick={() => handleDeleteFeedback(fb.id || fb.ID)}
                                                    style={{
                                                        padding: '4px 12px',
                                                        fontSize: '12px',
                                                        background: 'rgba(255, 51, 102, 0.1)',
                                                        borderColor: 'var(--bad)',
                                                        color: 'var(--bad)'
                                                    }}
                                                >
                                                    Delete
                                                </button>
                                            )}
                                        </div>
                                        <p style={{ color: 'var(--text)', lineHeight: '1.6', whiteSpace: 'pre-wrap' }}>
                                            {fb.feedback}
                                        </p>
                                    </div>
                                ))
                            ) : (
                                <p className="sub">No feedback yet. Be the first to add feedback!</p>
                            )}
                        </div>

                        <div className="sep mt-24"></div>

                        <div className="add-feedback-form mt-24">
                            <div className="h-title" style={{ fontSize: '16px' }}>Add New Feedback</div>
                            <form onSubmit={handleSubmitFeedback} className="form mt-12">
                                <div className="field">
                                    <label className="label" htmlFor="newFeedback">Feedback</label>
                                    <textarea
                                        className="input textarea"
                                        id="newFeedback"
                                        rows="4"
                                        value={newFeedback}
                                        onChange={(e) => setNewFeedback(e.target.value)}
                                        placeholder="Enter your feedback here..."
                                        required
                                    ></textarea>
                                </div>
                                <button className="btn primary" type="submit" disabled={submitting}>
                                    {submitting ? 'Submitting...' : 'Submit Feedback'}
                                </button>
                            </form>
                        </div>
                    </section>
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

export default FeedbackPage;

