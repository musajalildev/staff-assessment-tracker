import { Link, useParams } from 'react-router-dom';
import Layout from '../components/Layout';
import { useState, useEffect } from 'react';
import { assessmentAPI, userAPI } from '../services/api';
import FeedbackSection from '../components/FeedbackSection';
import { useNavigate } from "react-router-dom";

function Test() {
    const { moduleId, assessmentId } = useParams();
    const [assessment, setAssessment] = useState(null);
    const [users, setUsers] = useState([]);
    const [progress, setProgress] = useState(0);
    const [progressSequence, setProgressSequence] = useState([]);
    const [type, setType] = useState("");
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    // Loads the relevant Assessment
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
                setUsers(usersRes.data || []);
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
        /*switch (assessment.type) {
          case "EXAM"
        }/**/
        const progressMap = {
            "CREATED": 1,
            "CHECKED": 2,
            "NEEDS_CHANGES": 3,
            "TEST_TAKING_PLACE": 4,
            "MARKING_STANDARDISED": 5,
            "MARKED": 6,
            "RESULTS_RETURNED": 7,
            "COMPLETE": 8
        };
        setProgress(progressMap[assessment.progress] || 0);



    }, [assessment]);
    // Code to Progress Assessments
    const progressAssessment = async () => {
        if (!assessment) return;
        if (assessment.progress == "CHECKED") {
            navigate("/test/feedback/" + assessmentId)
            return
        }

        const progressSequence = [
            "CREATED", "CHECKED", "NEEDS_CHANGES", "TEST_TAKING_PLACE",
            "MARKING_STANDARDISED", "MARKED", "RESULTS_RETURNED", "COMPLETE"
        ];


        const currentIndex = progressSequence.indexOf(assessment.progress);
        let nextProgress = currentIndex < progressSequence.length - 1
            ? progressSequence[currentIndex + 1]
            : progressSequence[1]; // Loop back to CHECKED if at end Test feature
        if (assessment.progress == "NEEDS_CHANGES") {
            console.log("needs_changes");
            nextProgress = "CHECKED";
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


    return (
        <Layout>
            <header className="header">
                <div className="h-title">{assessment.title || 'Untitled Assessment'} • {type}</div>
                <div className="actions">
                    <Link className="btn" to={`/modules/${moduleId}`}>Back to Module</Link>
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
                        {assessment.autoGraded && <span className="pill">Auto Graded</span>}
                    </div>
                </div>
                <div className="card">
                    <div className="label">Workflow</div>
                    <div className="timeline mt-12">
                        <div className={(() => { if (progress > 1) return "step done"; if (progress == 1) return "step active"; return "step" })()}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Test created</div>
                                <div className="meta">By Setter</div>
                            </div>
                        </div>
                        <div className={(() => { if (progress > 2) return "step done"; if (progress == 2) return "step active"; return "step" })()}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Checked</div>
                                <div className="meta">Checker approves or requests changes</div>
                            </div>
                        </div>
                        <div className={(() => { if (progress > 3) return "step done"; if (progress == 3) return "step active"; return "step" })()}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Needs Changes</div>
                                <div className="meta">Visible in VLE</div>
                            </div>
                        </div>
                        <div className={(() => { if (progress > 4) return "step done"; if (progress == 4) return "step active"; return "step" })()}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Test Taking Place</div>
                                <div className="meta">Window closes</div>
                            </div>
                        </div>
                        {assessment.type == "TEST_TEAM_MARKER" ? <div className={(() => { if (progress > 5) return "step done"; if (progress == 5) return "step active"; return "step" })()}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Standardisation (if team)</div>
                            </div>
                        </div> : ""
                        }
                        <div className={(() => { if (progress > 6) return "step done"; if (progress == 6) return "step active"; return "step" })()}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Marking</div>
                            </div>
                        </div>
                        <div className={progress > 7 ? "step done" : progress === 7 ? "step active" : "step"}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Results Returned</div>
                                <div className="meta">Complete</div>
                            </div>
                        </div>
                        <div className={progress > 8 ? "step done" : progress === 8 ? "step active" : "step"}>
                            <div className="dot"></div>
                            <div>
                                <div className="title">Complete</div>
                                <div className="meta">Assessment finished</div>
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

export default Test;

