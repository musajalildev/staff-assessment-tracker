import { Link, useParams } from 'react-router-dom';
import Layout from '../components/Layout';

function AssessmentDetail() {
  const { moduleId, assessmentId } = useParams();

  return (
    <Layout>
      <header className="header">
        <div className="h-title">Assignment 1 • Coursework</div>
        <div className="actions">
          <Link className="btn" to={`/modules/${moduleId}`}>Back to Module</Link>
          <button className="btn primary">Progress to Next Stage</button>
        </div>
      </header>

      <section className="grid two">
        <div className="card">
          <div className="label">People</div>
          <div className="mt-12">
            <div><strong>Setter:</strong> John Smith</div>
            <div className="mt-12"><strong>Checker:</strong> Anna Lee</div>
            <div className="mt-12"><strong>Moderator:</strong> (module default)</div>
            <div className="mt-12"><strong>External Examiner:</strong> —</div>
          </div>
          <div className="sep"></div>
          <div className="label mt-12">Details</div>
          <div className="mt-12">
            <span className="pill">Marked by team</span>
            <span className="pill">Weight: 20%</span>
            <span className="pill">Due: 10 Dec</span>
          </div>
        </div>
        <div className="card">
          <div className="label">Workflow</div>
          <div className="timeline mt-12">
            <div className="step done">
              <div className="dot"></div>
              <div>
                <div className="title">Spec created</div>
                <div className="meta">By Setter</div>
              </div>
            </div>
            <div className="step active">
              <div className="dot"></div>
              <div>
                <div className="title">Checked</div>
                <div className="meta">Checker approves or requests changes</div>
              </div>
            </div>
            <div className="step">
              <div className="dot"></div>
              <div>
                <div className="title">Spec released to students</div>
                <div className="meta">Visible in VLE</div>
              </div>
            </div>
            <div className="step">
              <div className="dot"></div>
              <div>
                <div className="title">Submission deadline</div>
                <div className="meta">Window closes</div>
              </div>
            </div>
            <div className="step">
              <div className="dot"></div>
              <div>
                <div className="title">Standardisation (if team)</div>
              </div>
            </div>
            <div className="step">
              <div className="dot"></div>
              <div>
                <div className="title">Marking</div>
              </div>
            </div>
            <div className="step">
              <div className="dot"></div>
              <div>
                <div className="title">Moderation</div>
              </div>
            </div>
            <div className="step">
              <div className="dot"></div>
              <div>
                <div className="title">Feedback returned</div>
              </div>
            </div>
            <div className="step">
              <div className="dot"></div>
              <div>
                <div className="title">Marks approved</div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <section className="grid mt-24">
        <div className="card">
          <div className="h-title" style={{ fontSize: '18px' }}>Actions</div>
          <div className="mt-12" style={{ display: 'flex', gap: '10px', flexWrap: 'wrap' }}>
            <button className="btn">Request Changes</button>
            <button className="btn">Assign Different Checker</button>
            <button className="btn">Upload Files</button>
            <button className="btn">Add Note</button>
          </div>
        </div>
      </section>
    </Layout>
  );
}

export default AssessmentDetail;

