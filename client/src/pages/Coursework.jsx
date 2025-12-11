import { Link, useParams } from 'react-router-dom';
import Layout from '../components/Layout';
import { useState, useEffect } from 'react';
import { assessmentAPI, userAPI, assignedUserAPI } from '../services/api';
import FeedbackSection from '../components/FeedbackSection';
import { useNavigate } from "react-router-dom";
import { getCurrentUser, canReverseStages } from '../utils/permissions';

function CourseworkDetail() {
    const { moduleId, assessmentId } = useParams();
    const [assessment, setAssessment] = useState(null);
    const [users, setUsers] = useState([]);
    const [assignedUsers, setAssignedUsers] = useState([]);
    const [currentUser, setCurrentUser] = useState(null);
    const [progress, setProgress] = useState(0);
    const [progressSequence, setProgressSequence] = useState([]);
    const [type, setType] = useState("");
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    // Loads the relevant Assessment
    useEffect(() => {
        const loadAssessment = async () => {

            if (!assessmentId || isNaN(parseInt(assessmentId))) {
                setError('Invalid assessment ID');
                setLoading(false);
                return;
            }

            try {
                setLoading(true);
                const assessmentIdToUse = assessmentId || 1; // Fallback to 1 if not provided

                const user = getCurrentUser();
                setCurrentUser(user);

                const [assessmentRes, usersRes, assignedRes] = await Promise.all([
                    assessmentAPI.getById(assessmentIdToUse),
                    userAPI.getAll().catch(() => ({ data: [] })),
                    assignedUserAPI.getAll().catch(() => ({ data: [] }))
                ]);

                setAssessment(assessmentRes.data);
                setUsers(usersRes.data || []);
                setAssignedUsers(assignedRes.data || []);
            } catch (err) {
                setError('Failed to load assessment');
                console.error('Error loading assessment:', err);
            } finally {
                setLoading(false);
            }
        };

        loadAssessment();
    }, [assessmentId]);

    // Sets up maps for progress and type
    useEffect(() => {
        if (!assessment) return;
        const typeMap = {
            "EXAM": "Exam",
            "COURSEWORK": "Coursework",
            "TEST_AUTOGRADED": "Test (Autograded)",
            "TEST_SINGLE_MARKER": "Test (Single Marker)",
            "TEST_TEAM_MARKER": "Test (Team Marker)"
        };
        setType(typeMap[assessment.type] || assessment.type || "N/A");

        // Coursework-specific progress mapping
        const progressMap = {
            "SPEC_CREATED": 1,
            "SPEC_CHECKED": 2,
            "MODIFICATIONS_NEEDED": 3,
            "SPEC_RELEASED": 4,
            "DEADLINE_PASSED": 5,
            "STANDARDISATION": 6,
            "MARKING": 7,
            "MODERATION": 8,
            "FEEDBACK_RETURNED": 9
        };
        setProgress(progressMap[assessment.progress] || 0);
    }, [assessment]);

    // Code to Progress Assessments
    const progressAssessment = async () => {
        if (!assessment) return;
        if ((assessment.progress == "CHECKED" || assessment.progress == "EXAM_OFFICER_CHECKED" || assessment.progress == "EXTERNAL_EXAMINER_CHECK" || assessment.progress == "SETTER_FORMAL_RESPONSE")) {
            navigate("/feedback/" + assessmentId)
            return
        }

        const progressSequence = [
            "SPEC_CREATED", "SPEC_CHECKED", "MODIFICATIONS_NEEDED", "SPEC_RELEASED",
            "DEADLINE_PASSED", "STANDARDISATION", "MARKING", "MODERATION", "FEEDBACK_RETURNED"
        ];

        const currentIndex = progressSequence.indexOf(assessment.progress);
        let nextProgress = currentIndex < progressSequence.length - 1
            ? progressSequence[currentIndex + 1]
            : progressSequence[progressSequence.length - 1];

        if (assessment.progress == "MODIFICATIONS_NEEDED") {
            console.log("modifications_needed");
            nextProgress = "SPEC_CHECKED";
        }

        const updatedAssessment = {
            ...assessment,
            progress: nextProgress
        };

        try {
            const result = await assessmentAPI.update(assessment.id || assessment.ID, updatedAssessment);
            setAssessment(result.data);
        } catch (err) {
            console.error('Error updating assessment:', err);
            alert('Failed to update assessment progress');
        }
    };

    // Code to Return to Previous State
    const returnToPreviousState = async () => {
        if (!assessment) return;

        const progressSequence = [
            "SPEC_CREATED", "SPEC_CHECKED", "MODIFICATIONS_NEEDED", "SPEC_RELEASED",
            "DEADLINE_PASSED", "STANDARDISATION", "MARKING", "MODERATION", "FEEDBACK_RETURNED"
        ];

        const currentIndex = progressSequence.indexOf(assessment.progress);
        if (currentIndex <= 0) {
            alert('Already at the first stage');
            return;
        }

        const previousProgress = progressSequence[currentIndex - 1];

        const updatedAssessment = {
            ...assessment,
            progress: previousProgress
        };

        try {
            const result = await assessmentAPI.update(assessment.id || assessment.ID, updatedAssessment);
            setAssessment(result.data);
        } catch (err) {
            console.error('Error updating assessment:', err);
            alert('Failed to return to previous state');
        }
    };

    const getUserName = (user) => {
        if (!user) return 'N/A';
        if (typeof user === 'string') return user;
        return user.username || user.email || 'N/A';
    };

    // Check if user can see return to previous state button
    // User must be Teaching Support Team OR Exams Officer in admin view (Exam Officer View)
    const canReturnToPreviousState = () => {
        if (!currentUser) {
            console.log('Permission check failed: missing currentUser', { currentUser });
            return false;
        }
        
        const userId = currentUser?.id || currentUser?.userID || currentUser?.ID;
        const username = currentUser?.username;
        const currentView = currentUser?.selectedUserType || currentUser?.selectedRole;
        
        // Use the permission utility function which handles:
        // - Teaching Support: always can reverse
        // - Exams Officers: can reverse ONLY when in Exam Officer View (admin view)
        return canReverseStages(assignedUsers || [], userId, username, currentView);
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

    return (
        <Layout>
            <header className="header">
                <div className="h-title">{assessment.title || 'Untitled Assessment'} • {type}</div>
                <div className="actions">
                    <Link className="btn" to={`/modules/${moduleId}`}>Back to Module</Link>
                    {canReturnToPreviousState() && (
                        <button 
                            className="btn" 
                            onClick={returnToPreviousState}
                            disabled={!assessment || (assessment.progress === "SPEC_CREATED")}
                        >
                            Return to Previous State
                        </button>
                    )}
                    <button className="btn primary" onClick={progressAssessment}>Progress to Next Stage</button>
                </div>
            </header>

            <section className="grid two">
                <div className="card">
                    <div className="label">People</div>
                    <div className="mt-12">
                        <div><strong>Setter:</strong> {getUserName(assessment.setter)}</div>
                        <div className="mt-12"><strong>Checker:</strong> {getUserName(assessment.checker)}</div>
                        <div className="mt-12"><strong>Assessment ID:</strong> {assessment.id || assessment.ID}</div>
                    </div>
                    <div className="sep"></div>
                    <div className="label mt-12">Details</div>
                    <div className="mt-12">
                        <span className="pill">Type: {type}</span>
                        <span className="pill">Progress: {assessment.progress || 'N/A'}</span>
                        {assessment.teamMarked && <span className="pill">Team Marked</span>}
                    </div>
                </div>
                <div className="card">
                    <div className="label">Workflow</div>
                    <div className="timeline mt-12">
                        <div className={(() => { if (progress > 1) return "step done"; if (progress == 1) return "step active"; return "step" })()}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Spec Created</div>
                                <div className="meta">By Setter</div>
                            </div>
                        </div>
                        <div className={(() => { if (progress > 2) return "step done"; if (progress == 2) return "step active"; return "step" })()}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Spec Checked</div>
                                <div className="meta">By Checker</div>
                            </div>
                        </div>
                        <div className={(() => { if (progress > 3) return "step done"; if (progress == 3) return "step active"; return "step" })()}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Modifications Made/Approved</div>
                                <div className="meta">Setter & Checker loop</div>
                            </div>
                        </div>
                        <div className={(() => { if (progress > 4) return "step done"; if (progress == 4) return "step active"; return "step" })()}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Spec Released to Students</div>
                                <div className="meta">Available in VLE</div>
                            </div>
                        </div>
                        <div className={(() => { if (progress > 5) return "step done"; if (progress == 5) return "step active"; return "step" })()}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Deadline Passed</div>
                                <div className="meta">Manual progression</div>
                            </div>
                        </div>
                        {assessment.teamMarked ? <div className={(() => { if (progress > 6) return "step done"; if (progress == 6) return "step active"; return "step" })()}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Standardisation (if team)</div>
                            </div>
                        </div> : ""
                        }
                        <div className={(() => { if (progress > 7) return "step done"; if (progress == 7) return "step active"; return "step" })()}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Marking</div>
                                <div className="meta">By module staff</div>
                            </div>
                        </div>
                        <div className={(() => { if (progress > 8) return "step done"; if (progress == 8) return "step active"; return "step" })()}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Moderation</div>
                                <div className="meta">By moderator</div>
                            </div>
                        </div>
                        <div className={progress > 9 ? "step done" : progress === 9 ? "step active" : "step"}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Feedback Returned</div>
                                <div className="meta">Complete</div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <section className="grid mt-24">
                <div className="card">
                    <div className="h-title" style={{ fontSize: '18px' }}>Actions</div>
                    <div className="mt-12" style={{ display: 'flex', gap: '10px', flexWrap: 'wrap' }}>
                        <button className="btn" onClick={progressAssessment}>Update Progress</button>
                        <Link className="btn" to={`/modules/${moduleId}`}>Back to Module</Link>
                    </div>
                </div>
            </section>

            {assessment && (
                <FeedbackSection
                    assessment={assessment}
                    onFeedbackAdded={() => {
                        // Optionally reload assessment data when feedback is added
                        console.log('Feedback added, reloading assessment...');
                    }}
                />
            )}
        </Layout>
    );
}

export default CourseworkDetail;