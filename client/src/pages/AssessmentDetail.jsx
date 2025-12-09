import { Link, useParams } from 'react-router-dom';
import Layout from '../components/Layout';
import { useState, useEffect } from 'react';
import { assessmentAPI, userAPI, assignedUserAPI, feedbackAPI } from '../services/api';
import FeedbackSection from '../components/FeedbackSection';
import { useNavigate } from "react-router-dom";
import { getWorkflowStages, getNextStage, getPreviousStage, getStageInfo, canProgressStage } from '../utils/workflows';
import { getCurrentUser, canReverseStages, canOverrideProgress } from '../utils/permissions';

function AssessmentDetail() {
  const { moduleId, assessmentId } = useParams();
  const [assessment, setAssessment] = useState(null);
  const [users, setUsers] = useState([]);
  const [progress, setProgress] = useState(0);
  const [progressSequence, setProgressSequence] = useState([]);
  const [type, setType] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();
  const [assignedUsers, setAssignedUsers] = useState([]);
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [comment, setComment] = useState('');
  const [showCommentModal, setShowCommentModal] = useState(false);
  const [pendingAction, setPendingAction] = useState(null);
  const [currentUser, setCurrentUser] = useState(null);

  // Loads the relevant Assessment
  useEffect(() => {
    const loadAssessment = async () => {
      try {
        setLoading(true);
        const user = getCurrentUser();
        setCurrentUser(user);
        
        const assessmentIdToUse = assessmentId || 1;
        
        const [assessmentRes, usersRes, assignedRes] = await Promise.all([
          assessmentAPI.getById(assessmentIdToUse),
          userAPI.getAll().catch(() => ({ data: [] })),
          assignedUserAPI.getAll().catch(() => ({ data: [] }))
        ]);

        setAssessment(assessmentRes.data);
        setUsers(usersRes.data || []);
        setAssignedUsers(assignedRes.data || []);

        // Load history/logs (when backend supports it)
        // For now, create mock history from assessment data
        if (assessmentRes.data?.logs) {
          setHistory(assessmentRes.data.logs);
        } else {
          // Mock history for demonstration
          setHistory([]);
        }
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

  const getUserName = (user) => {
    if (!user) return 'N/A';
    if (typeof user === 'string') return user;
    return user.username || user.email || 'N/A';
  };

  const getTypeLabel = (type) => {
    const typeMap = {
      "EXAM": "Exam",
      "COURSEWORK": "Coursework",
      "TEST_AUTOGRADED": "Test (Autograded)",
      "TEST_SINGLE_MARKER": "Test (Single Marker)",
      "TEST_TEAM_MARKER": "Test (Team Marker)"
    };
    return typeMap[type] || type || "N/A";
  };

  const handleProgress = async (requireComment = false) => {
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

    if (requireComment && !comment.trim()) {
      alert('Please provide a comment for this action');
      return;
    }

    const nextStage = getNextStage(assessment.progress, assessment.type);
    if (!nextStage) {
      alert('Already at final stage');
      return;
    }

    const updatedAssessment = {
      ...assessment,
      progress: nextStage,
      comment: comment.trim() || undefined
    };

    try {
      const result = await assessmentAPI.update(assessment.id || assessment.ID, updatedAssessment);
      setAssessment(result.data);
      setComment('');
      setShowCommentModal(false);
      setPendingAction(null);
      
      // Reload to get updated history
      const assessmentRes = await assessmentAPI.getById(assessment.id || assessment.ID);
      if (assessmentRes.data?.logs) {
        setHistory(assessmentRes.data.logs);
      }
    } catch (err) {
      console.error('Error updating assessment:', err);
      alert('Failed to update assessment progress');
    }
  };

  const handleReverse = async () => {
    if (!assessment) return;

    if (!window.confirm('Are you sure you want to reverse this stage? This action will undo the last progression.')) {
      return;
    }

    const previousStage = getPreviousStage(assessment.progress, assessment.type);
    if (!previousStage) {
      alert('Already at first stage');
      return;
    }

    const updatedAssessment = {
      ...assessment,
      progress: previousStage
    };

    try {
      const result = await assessmentAPI.update(assessment.id || assessment.ID, updatedAssessment);
      setAssessment(result.data);
      
      // Reload to get updated history
      const assessmentRes = await assessmentAPI.getById(assessment.id || assessment.ID);
      if (assessmentRes.data?.logs) {
        setHistory(assessmentRes.data.logs);
      }
    } catch (err) {
      console.error('Error reversing assessment:', err);
      alert('Failed to reverse assessment progress');
    }
  };

  const requestProgress = (requireComment = false) => {
    setPendingAction(() => () => handleProgress(requireComment));
    if (requireComment) {
      setShowCommentModal(true);
    } else {
      handleProgress(false);
    }
  };

  const canProgress = () => {
    if (!assessment || !currentUser) return false;
    
    const userId = currentUser.id || currentUser.userID;
    const username = currentUser.username;
    const userRoles = assignedUsers
      .filter(au => au.user?.userID === userId || au.user?.username === username)
      .map(au => au.role);
    
    const isSetter = assessment.setter?.userID === userId || assessment.setter?.username === username;
    const isChecker = assessment.checker?.userID === userId || assessment.checker?.username === username;
    const isModerator = false; // Would need module roles data
    const isModuleStaff = false; // Would need module roles data
    const canOverride = canOverrideProgress(assignedUsers, userId, username, currentUser?.selectedUserType || currentUser?.selectedRole);
    
    return canProgressStage(
      assessment.progress,
      assessment.type,
      userRoles[0],
      isSetter,
      isChecker,
      isModerator,
      isModuleStaff,
      canOverride
    );
  };

  const canReverse = () => {
    if (!assessment || !currentUser) return false;
    const userId = currentUser.id || currentUser.userID;
    const username = currentUser.username;
    return canReverseStages(assignedUsers, userId, username, currentUser?.selectedUserType || currentUser?.selectedRole);
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

  const workflowStages = getWorkflowStages(assessment.type);
  const currentStageIndex = workflowStages.findIndex(s => s.value === assessment.progress);
  const typeLabel = getTypeLabel(assessment.type);

  // Determine if comment is required (for modifications, feedback, etc.)
  const requiresComment = ['NEEDS_CHANGES', 'SETTER_MODIFICATIONS', 'EXTERNAL_EXAMINER_FEEDBACK', 'SETTER_RESPONSE'].includes(assessment.progress);

  return (
    <Layout>
      <header className="header">
        <div className="h-title">{assessment.title || 'Untitled Assessment'} • {typeLabel}</div>
        <div className="actions">
          <Link 
            className="btn" 
            to={`/modules/${moduleId}`}
            onClick={() => {
              // Force a page reload to refresh module data
              // This ensures assessments are reloaded after updates
              setTimeout(() => window.location.reload(), 100);
            }}
          >
            Back to Module
          </Link>
          {canProgress() && (
            <button 
              className="btn primary" 
              onClick={() => requestProgress(requiresComment)}
            >
              Progress to Next Stage
            </button>
          )}
          {canReverse() && (
            <button 
              className="btn" 
              onClick={handleReverse}
              style={{ color: 'var(--bad)' }}
            >
              Reverse Stage
            </button>
          )}
        </div>
      </header>

      {showCommentModal && (
        <div style={{
          position: 'fixed',
          top: 0,
          left: 0,
          right: 0,
          bottom: 0,
          background: 'rgba(0, 0, 0, 0.5)',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          zIndex: 1000
        }}>
          <div className="card" style={{ maxWidth: '500px', width: '90%' }}>
            <h3 style={{ marginBottom: '16px' }}>Add Comment</h3>
            <p className="sub" style={{ marginBottom: '12px' }}>
              A comment is required for this action. Please provide a summary of changes or feedback.
            </p>
            <textarea
              className="input"
              value={comment}
              onChange={(e) => setComment(e.target.value)}
              placeholder="Enter comment..."
              rows={5}
              style={{ width: '100%', marginBottom: '16px' }}
            />
            <div style={{ display: 'flex', gap: '12px', justifyContent: 'flex-end' }}>
              <button 
                className="btn" 
                onClick={() => {
                  setShowCommentModal(false);
                  setComment('');
                  setPendingAction(null);
                }}
              >
                Cancel
              </button>
              <button 
                className="btn primary" 
                onClick={() => pendingAction && pendingAction()}
                disabled={!comment.trim()}
              >
                Confirm
              </button>
            </div>
          </div>
        </div>
      )}

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
            <span className="pill">Type: {typeLabel}</span>
            <span className="pill">Progress: {getStageInfo(assessment.progress, assessment.type).label}</span>
            {assessment.teamMarked && <span className="pill">Team Marked</span>}
            {assessment.autoGraded && <span className="pill">Auto Graded</span>}
          </div>
        </div>
        <div className="card">
          <div className="label">Workflow</div>
          <div className="timeline mt-12">
            {workflowStages.map((stage, index) => {
              const isDone = index < currentStageIndex;
              const isActive = index === currentStageIndex;
              
              return (
                <div 
                  key={stage.value} 
                  className={isDone ? "step done" : isActive ? "step active" : "step"}
                >
                  <div className="dot"></div>
                  <div>
                    <div className="title">{stage.label}</div>
                    {stage.description && (
                      <div className="meta">{stage.description}</div>
                    )}
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </section>

      {/* History Timeline */}
      <section className="card mt-24">
        <div className="h-title" style={{ fontSize: '18px' }}>History</div>
        {history.length > 0 ? (
          <div className="timeline mt-12">
            {history.map((log, index) => (
              <div key={index} className="step done">
                <div className="dot"></div>
                <div>
                  <div className="title">
                    {log.previousState ? `${log.previousState} → ${log.newState}` : log.actionType || 'Action'}
                  </div>
                  <div className="meta">
                    {getUserName(log.user)} • {log.logTime ? new Date(log.logTime).toLocaleString() : 'Unknown time'}
                  </div>
                  {log.comment && (
                    <div className="sub" style={{ marginTop: '4px' }}>{log.comment}</div>
                  )}
                </div>
              </div>
            ))}
          </div>
        ) : (
          <p className="sub mt-12">No history available yet.</p>
        )}
      </section>

      <section className="grid mt-24">
        <div className="card">
          <div className="h-title" style={{ fontSize: '18px' }}>Actions</div>
          <div className="mt-12" style={{ display: 'flex', gap: '10px', flexWrap: 'wrap' }}>
            {canProgress() && (
              <button 
                className="btn primary" 
                onClick={() => requestProgress(requiresComment)}
              >
                {requiresComment ? 'Progress (Comment Required)' : 'Progress to Next Stage'}
              </button>
            )}
            {canReverse() && (
              <button 
                className="btn" 
                onClick={handleReverse}
                style={{ color: 'var(--bad)' }}
              >
                Reverse Stage
              </button>
            )}
            <Link 
              className="btn" 
              to={`/modules/${moduleId}`}
              onClick={() => {
                // Force a page reload to refresh module data
                setTimeout(() => window.location.reload(), 100);
              }}
            >
              Back to Module
            </Link>
          </div>
          {!canProgress() && !canReverse() && (
            <p className="sub mt-12">You don't have permission to modify this assessment's progress.</p>
          )}
        </div>
      </section>

      {assessment && (
        <FeedbackSection
          assessment={assessment}
          onFeedbackAdded={() => {
            console.log('Feedback added, reloading assessment...');
            // Reload assessment
            assessmentAPI.getById(assessment.id || assessment.ID).then(res => {
              setAssessment(res.data);
            });
          }}
        />
      )}
    </Layout>
  );
}

export default AssessmentDetail;
