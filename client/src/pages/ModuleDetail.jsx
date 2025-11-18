import { Link, useParams } from 'react-router-dom';
import Layout from '../components/Layout';

function ModuleDetail() {
  const { id } = useParams();

  return (
    <Layout>
      <header className="header">
        <div className="h-title">CSC101 • Programming 1</div>
        <div className="actions">
          <Link className="btn" to="/modules">Back</Link>
          <Link className="btn" to={`/modules/${id}/edit`}>Edit Module</Link>
          <Link className="btn primary" to={`/modules/${id}/assessments/new`}>Add Assessment</Link>
        </div>
      </header>

      <section className="grid two">
        <div className="card">
          <div className="label">Module Lead</div>
          <div className="mt-12">Dr. Jane Doe</div>
          <div className="sep"></div>
          <div className="label mt-12">Moderator</div>
          <div className="mt-12">Anna Lee</div>
          <div className="sep"></div>
          <div className="label mt-12">Staff</div>
          <ul className="mt-12" style={{ display: 'grid', gap: '8px', listStyle: 'none' }}>
            <li>John Smith (Setter)</li>
            <li>Mary Chan (Staff)</li>
          </ul>
        </div>
        <div className="card">
          <div className="h-title" style={{ fontSize: '18px' }}>Assessments</div>
          <table className="table mt-12">
            <thead>
              <tr>
                <th>Title</th>
                <th>Type</th>
                <th>Stage</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>Assignment 1</td>
                <td>Coursework</td>
                <td><span className="badge warn">Checking</span></td>
                <td><Link to={`/modules/${id}/assessments/1`}>Open →</Link></td>
              </tr>
              <tr>
                <td>Midterm Test</td>
                <td>Test</td>
                <td><span className="badge ok">Marking</span></td>
                <td><Link to={`/modules/${id}/assessments/2`}>Open →</Link></td>
              </tr>
              <tr>
                <td>Final Exam</td>
                <td>Exam</td>
                <td><span className="badge">External Review</span></td>
                <td><Link to={`/modules/${id}/assessments/3`}>Open →</Link></td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>
    </Layout>
  );
}

export default ModuleDetail;

