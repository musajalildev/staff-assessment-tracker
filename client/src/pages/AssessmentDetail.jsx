import { Link, useParams } from 'react-router-dom';
import Layout from '../components/Layout';
import { useState, useEffect } from 'react';
import { assessmentAPI } from '../services/api';
import { useNavigate } from "react-router-dom";

function AssessmentDetail() {
  const { moduleId, assessmentId } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  // Loads the assessment and redirects to type-specific page
  useEffect(() => {
    const loadAssessment = async () => {
      try {
        setLoading(true);
        const assessmentIdToUse = assessmentId || 1;

        const assessmentRes = await assessmentAPI.getById(assessmentIdToUse);

        // Redirect to type-specific page based on assessment type
        const assessmentType = assessmentRes.data?.type;
        if (assessmentType === "COURSEWORK") {
          navigate(`/modules/${moduleId}/coursework/${assessmentIdToUse}`, { replace: true });
          return;
        }
        if (assessmentType === "EXAM") {
          navigate(`/modules/${moduleId}/exam/${assessmentIdToUse}`, { replace: true });
          return;
        }
        if (assessmentType && (assessmentType.includes("TEST") || assessmentType === "TEST_AUTOGRADED" || assessmentType === "TEST_SINGLE_MARKER" || assessmentType === "TEST_TEAM_MARKER")) {
          navigate(`/modules/${moduleId}/test/${assessmentIdToUse}`, { replace: true });
          return;
        }

        // If type is unknown, show error
        setError('Unknown assessment type');
      } catch (err) {
        setError('Failed to load assessment');
        console.error('Error loading assessment:', err);
      } finally {
        setLoading(false);
      }
    };

    loadAssessment();
  }, [assessmentId, moduleId, navigate]);

  if (loading) {
    return (
      <Layout>
        <div className="card">
          <p>Loading assessment...</p>
        </div>
      </Layout>
    );
  }

  if (error) {
    return (
      <Layout>
        <div className="content">
          <h2>{error}</h2>
          <Link to={`/modules/${moduleId}`}>Back to Module</Link>
        </div>
      </Layout>
    );
  }

  return null;
}
export default AssessmentDetail;
