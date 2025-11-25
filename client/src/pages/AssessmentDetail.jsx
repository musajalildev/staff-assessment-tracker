import { Link, useParams } from 'react-router-dom';
import Layout from '../components/Layout';
import axios from 'axios';
import { useState, useEffect } from 'react';


function AssessmentDetail() {
  const { moduleId, assessmentID } = useParams();
  const [assessment, setAssessment] = useState({});
  const [progress, setProgress] = useState({});
  const [type, setType] = useState("");

  useEffect(() => {
    axios.get('http://localhost:8080/api/assessment/1').then((data) => setAssessment(data.data));
  }, []);
  useEffect(() => {
    switch (assessment.progress) {
      case "CREATED":
        setProgress(1);
        break;
      case "CHECKED":
        setProgress(2);
        break;
      case "NEEDS_CHANGES":
        setProgress(3);
        break;
      case "TEST_TAKING_PLACE":
        setProgress(4);
        break;
      case "MARKING_STANDARDISED":
        setProgress(5);
        break;
      case "MARKED":
        setProgress(6);
        break;
      case "RESULTS_RETURNED":
        setProgress(7);
        break;
      case "COMPLETE":
        setProgress(8);
        break;
    }
    switch (assessment.type) {
      case "EXAM":
        setType("Exam");
        break;
      case "COURSEWORK":
        setType("Coursework");
        break;
      case "TEST_AUTOGRADED":
      case "TEST_SINGLE_MARKER":
      case "TEST_TEAM_MARKER":
        setType("Test");
        break;
      default:
        setType("Error Type")
        break;
    }
    console.log(assessment);
    console.log(progress);
  }, [assessment]);

  const progressAssessment = async () => {
    switch (assessment.progress) {
      case "CREATED":
        assessment.progress = "CHECKED";
        break;
      case "CHECKED":
        assessment.progress = "NEEDS_CHANGES";
        break;
      case "NEEDS_CHANGES":
        assessment.progress = "TEST_TAKING_PLACE";
        break;
      case "TEST_TAKING_PLACE":
        assessment.progress = "MARKING_STANDARDISED";
        break;
      case "MARKING_STANDARDISED":
        assessment.progress = "MARKED";
        break;
      case "MARKED":
        assessment.progress = "RESULTS_RETURNED";
        break;
      case "RESULTS_RETURNED":
        assessment.progress = "COMPLETE";
        break;
      case "COMPLETE":
        assessment.progress = "CHECKED";
        break;
    }
    console.log("progress");
    const result = await axios.put('http://localhost:8080/api/assessment/1', assessment)
    console.log(result);
    setAssessment(result.data);
  }


  /*useEffect(() => {
    axios.get(`/api/assessment/${assessmentID}`)
      .then(response => {
        console.log("Full response:", response);   // logs headers, status, data
        console.log("Response data:", response.data); // logs just the body
      })
      .catch(error => console.error("API error:", error));
  }, [assessmentId]);
  /**/
  return (
    <Layout>
      <header className="header">
        <div className="h-title">{assessment.title} • {type}</div>
        <div className="actions">
          <Link className="btn" to={`/modules/${moduleId}`}>Back to Module</Link>
          <button className="btn primary" onClick={progressAssessment}>Progress to Next Stage</button>
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
            {/*is this needed*/}
            <span className="pill">Weight: 20%</span>
            <span className="pill">Due: 10 Dec</span>
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
            <div className={(() => { if (progress > 5) return "step done"; if (progress == 5) return "step active"; return "step" })()}>
              <div className="dot"></div>
              <div>
                <div className="title">Standardisation (if team)</div>
              </div>
            </div>
            <div className={(() => { if (progress > 6) return "step done"; if (progress == 6) return "step active"; return "step" })()}>
              <div className="dot"></div>
              <div>
                <div className="title">Marking</div>
              </div>
            </div>
            <div className={(() => { if (progress > 7) return "step done"; if (progress == 7) return "step active"; return "step" })()}>
              <div className="dot"></div>
              <div>
                <div className="title">Returned</div>
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
    </Layout >
  );
}

export default AssessmentDetail;

