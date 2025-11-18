import { Link } from 'react-router-dom';
import Layout from '../components/Layout';

function Dashboard() {
  return (
    <Layout>
      <header className="header">
        <div className="h-title">Overview</div>
        <div className="actions">
          <Link className="btn" to="/modules">View all modules</Link>
        </div>
      </header>

      <section className="grid three">
        <div className="card">
          <div className="label">Total Modules</div>
          <h2>28</h2>
          <p className="sub mt-12">All faculties • sorted by code</p>
        </div>
        <div className="card">
          <div className="label">Assessments in Progress</div>
          <h2>73</h2>
          <p className="sub mt-12">Across Coursework, Tests, Exams</p>
        </div>
        <div className="card">
          <div className="label">Actions for You</div>
          <div className="mt-12">
            <span className="pill">3 to check</span>
            <span className="pill">2 to moderate</span>
            <span className="pill">1 response needed</span>
          </div>
        </div>
      </section>

      <section className="grid two mt-24">
        <div className="card">
          <div className="h-title" style={{ fontSize: '18px' }}>Your Modules</div>
          <table className="table mt-12">
            <thead>
              <tr>
                <th>Code</th>
                <th>Title</th>
                <th>Role</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>CSC101</td>
                <td>Programming 1</td>
                <td>Lead</td>
                <td><Link to="/modules/1">Open →</Link></td>
              </tr>
              <tr>
                <td>CSC212</td>
                <td>Data Structures</td>
                <td>Staff</td>
                <td><Link to="/modules/2">Open →</Link></td>
              </tr>
              <tr>
                <td>MAT150</td>
                <td>Discrete Math</td>
                <td>Moderator</td>
                <td><Link to="/modules/3">Open →</Link></td>
              </tr>
            </tbody>
          </table>
        </div>
        <div className="card">
          <div className="h-title" style={{ fontSize: '18px' }}>Pending Actions</div>
          <div className="timeline mt-12">
            <div className="step active">
              <div className="dot"></div>
              <div>
                <div className="title">Assignment 1 needs checking</div>
                <div className="meta">CSC101 • Coursework</div>
              </div>
            </div>
            <div className="step">
              <div className="dot"></div>
              <div>
                <div className="title">Midterm ready for marking</div>
                <div className="meta">CSC212 • Test</div>
              </div>
            </div>
            <div className="step">
              <div className="dot"></div>
              <div>
                <div className="title">Final exam: external feedback required</div>
                <div className="meta">MAT150 • Exam</div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </Layout>
  );
}

export default Dashboard;

