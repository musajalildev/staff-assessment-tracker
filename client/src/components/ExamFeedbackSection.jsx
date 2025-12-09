import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { feedbackAPI } from '../services/api';

function FeedbackSection({ assessment, onFeedbackAdded }) {
    const { assessmentId } = useParams();
    const [feedbackList, setFeedbackList] = useState([]);
    const [newFeedback, setNewFeedback] = useState('');
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [submitting, setSubmitting] = useState(false);
    const [currentUser, setCurrentUser] = useState(null);

    useEffect(() => {
        const storedUser = localStorage.getItem('currentUser');
        if (storedUser) {
            setCurrentUser(JSON.parse(storedUser));
        }
    }, []);

    useEffect(() => {
        if (assessmentId) {
            loadFeedback();
        }
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

        const feedbackData = {
            feedback: newFeedback.trim(),
            assessmentID: parseInt(assessmentId)
        };

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

    if (loading) {
        return (
            <section className="card mt-24">
                <div className="h-title" style={{ fontSize: '18px' }}>Feedback & History</div>
                <p className="sub mt-12">Loading feedback...</p>
            </section>
        );
    }

    return (
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
    );
}

export default FeedbackSection;

