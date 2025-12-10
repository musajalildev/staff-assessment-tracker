import { Link, useParams } from 'react-router-dom';
import Layout from '../components/Layout';
import { useState, useEffect } from 'react';
import { assessmentAPI, userAPI, assignedUserAPI } from '../services/api';
import FeedbackSection from '../components/FeedbackSection';
import { useNavigate } from "react-router-dom";
import { getCurrentUser, isExamsOfficer } from '../utils/permissions';

function ExamDetail() {
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
            // Validate assessmentId before making requests
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

        // Exam-specific progress mapping
        const progressMap = {
            "EXAM_CREATED": 1,
            "CHECKED_BY_CHECKER": 2,
            "SETTER_MODIFICATIONS_1": 3,
            "EXAMS_OFFICER_CHECK": 4,
            "SETTER_MODIFICATIONS_2": 5,
            "EXTERNAL_EXAMINER_CHECK": 6,
            "SETTER_FORMAL_RESPONSE": 7,
            "EXAMS_OFFICER_FINAL_CHECK": 8,
            "EXAM_TAKES_PLACE": 9,
            "STANDARDISATION": 10,
            "MARKING": 11,
            "ADMIN_CHECK_TOTALS": 12,
            "MODERATION": 13
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
            "EXAM_CREATED", "CHECKED_BY_CHECKER", "SETTER_MODIFICATIONS_1", "EXAMS_OFFICER_CHECK",
            "SETTER_MODIFICATIONS_2", "EXTERNAL_EXAMINER_CHECK", "SETTER_FORMAL_RESPONSE",
            "EXAMS_OFFICER_FINAL_CHECK", "EXAM_TAKES_PLACE", "STANDARDISATION", "MARKING",
            "ADMIN_CHECK_TOTALS", "MODERATION"
        ];

        const currentIndex = progressSequence.indexOf(assessment.progress);
        let nextProgress = currentIndex < progressSequence.length - 1
            ? progressSequence[currentIndex + 1]
            : progressSequence[progressSequence.length - 1];

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
            "EXAM_CREATED", "CHECKED_BY_CHECKER", "SETTER_MODIFICATIONS_1", "EXAMS_OFFICER_CHECK",
            "SETTER_MODIFICATIONS_2", "EXTERNAL_EXAMINER_CHECK", "SETTER_FORMAL_RESPONSE", 
            "EXAMS_OFFICER_FINAL_CHECK", "EXAM_TAKES_PLACE", "STANDARDISATION", "MARKING", 
            "ADMIN_CHECK_TOTALS", "MODERATION"
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
    const canReturnToPreviousState = () => {
        if (!currentUser || !assignedUsers) {
            console.log('Permission check failed: missing currentUser or assignedUsers', { currentUser, assignedUsers });
            return false;
        }
        
        const userId = currentUser?.id || currentUser?.userID || currentUser?.ID;
        const username = currentUser?.username;
        
        // Check if user is Teaching Support Team
        const isTeachingSupport = currentUser?.userType === 'ROLE_TEACHING_SUPPORT' || 
                                  currentUser?.selectedUserType === 'ROLE_TEACHING_SUPPORT' ||
                                  currentUser?.primaryUserType === 'ROLE_TEACHING_SUPPORT';
        
        // Check if user has AssessmentRole ROLE_EXAM_OFFICER
        // AssessmentRolesDTO has userID directly, not nested in user object
        const userRoles = assignedUsers
            .filter(au => {
                // Check userID directly from DTO (AssessmentRolesDTO.userID)
                const auUserId = au.userID || au.user?.userID || au.user?.ID || au.user?.id;
                const auUsername = au.username || au.user?.username;
                // Compare as strings to handle UUID format
                return (auUserId && String(auUserId) === String(userId)) ||
                       (auUsername && auUsername === username);
            })
            .map(au => {
                // Handle role as enum object or string
                const role = au.role;
                return typeof role === 'string' ? role : (role?.name || String(role));
            });
        
        const hasExamOfficerRole = userRoles.some(r => String(r) === 'ROLE_EXAM_OFFICER' || String(r) === 'EXAM_OFFICER') ||
                                   isExamsOfficer(assignedUsers, userId, username);
        
        return isTeachingSupport || hasExamOfficerRole;
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
                            disabled={!assessment || (assessment.progress === "EXAM_CREATED")}
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
                        <div className="mt-12"><strong>External Examiner:</strong> {getUserName(assessment.externalExaminer)}</div>
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
                    <div className="timeline mt-12" style={{ maxHeight: '500px', overflowY: 'auto' }}>
                        <div className={(() => { if (progress > 1) return "step done"; if (progress == 1) return "step active"; return "step" })()}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Exam Created</div>
                                <div className="meta">By Setter</div>
                            </div>
                        </div>
                        <div className={(() => { if (progress > 2) return "step done"; if (progress == 2) return "step active"; return "step" })()}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Checked by Checker</div>
                                <div className="meta">Initial review</div>
                            </div>
                        </div>
                        <div className={(() => { if (progress > 3) return "step done"; if (progress == 3) return "step active"; return "step" })()}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Setter Modifications</div>
                                <div className="meta">After checker feedback</div>
                            </div>
                        </div>
                        <div className={(() => { if (progress > 4) return "step done"; if (progress == 4) return "step active"; return "step" })()}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Exams Officer Check</div>
                                <div className="meta">Review by exams officer</div>
                            </div>
                        </div>
                        <div className={(() => { if (progress > 5) return "step done"; if (progress == 5) return "step active"; return "step" })()}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Setter Modifications + Officer Approval</div>
                                <div className="meta">Final setter changes</div>
                            </div>
                        </div>
                        <div className={(() => { if (progress > 6) return "step done"; if (progress == 6) return "step active"; return "step" })()}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">External Examiner Checks & Feedback</div>
                                <div className="meta">External review (once only)</div>
                            </div>
                        </div>
                        <div className={(() => { if (progress > 7) return "step done"; if (progress == 7) return "step active"; return "step" })()}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Setter Responds Formally</div>
                                <div className="meta">Written response + changes</div>
                            </div>
                        </div>
                        <div className={(() => { if (progress > 8) return "step done"; if (progress == 8) return "step active"; return "step" })()}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Exams Officer Final Check</div>
                                <div className="meta">Sent to print - no more changes</div>
                            </div>
                        </div>
                        <div className={(() => { if (progress > 9) return "step done"; if (progress == 9) return "step active"; return "step" })()}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Exam Takes Place</div>
                                <div className="meta">Auto-progressed next working day</div>
                            </div>
                        </div>
                        {assessment.teamMarked ? <div className={(() => { if (progress > 10) return "step done"; if (progress == 10) return "step active"; return "step" })()}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Standardisation (if team)</div>
                            </div>
                        </div> : ""
                        }
                        <div className={(() => { if (progress > 11) return "step done"; if (progress == 11) return "step active"; return "step" })()}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Marking</div>
                                <div className="meta">By module staff</div>
                            </div>
                        </div>
                        <div className={(() => { if (progress > 12) return "step done"; if (progress == 12) return "step active"; return "step" })()}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Admin Team Check Marking Totals</div>
                                <div className="meta">Verification</div>
                            </div>
                        </div>
                        <div className={progress > 13 ? "step done" : progress === 13 ? "step active" : "step"}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Moderation</div>
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

export default ExamDetail;