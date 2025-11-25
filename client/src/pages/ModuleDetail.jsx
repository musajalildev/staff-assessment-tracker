
import { Link, useParams } from 'react-router-dom';
import Layout from '../components/Layout';

// Mock module data (should match Modules.jsx)
const modules = [
  {
    id: '1',
    code: 'CSC101',
    title: 'Programming 1',
    lead: 'Jane Doe',
    moderator: 'Anna Lee',
    staff: ['John Smith (Setter)', 'Mary Chan (Staff)'],
    assessments: [
      { id: 1, title: 'Assignment 1', type: 'Coursework', stage: 'Checking', stageClass: 'warn' },
      { id: 2, title: 'Midterm Test', type: 'Test', stage: 'Marking', stageClass: 'ok' },
      { id: 3, title: 'Final Exam', type: 'Exam', stage: 'External Review', stageClass: '' },
    ],
  },
  {
    id: '2',
    code: 'CSC212',
    title: 'Data Structures',
    lead: 'Juan Park',
    moderator: 'Jane Doe',
    staff: ['Anna Lee (Setter)', 'John Smith (Staff)'],
    assessments: [
      { id: 1, title: 'Assignment 1', type: 'Coursework', stage: 'Checking', stageClass: 'warn' },
      { id: 2, title: 'Final Project', type: 'Project', stage: 'Marking', stageClass: 'ok' },
    ],
  },
  {
    id: '3',
    code: 'MAT150',
    title: 'Discrete Math',
    lead: 'John Smith',
    moderator: 'Anna Lee',
    staff: ['Mary Chan (Setter)', 'Jane Doe (Staff)'],
    assessments: [
      { id: 1, title: 'Quiz 1', type: 'Quiz', stage: 'Checking', stageClass: 'warn' },
      { id: 2, title: 'Final Exam', type: 'Exam', stage: 'External Review', stageClass: '' },
    ],
  },
];

function ModuleDetail() {
  const { id } = useParams();
  const module = modules.find((m) => m.id === id);

  if (!module) {
    return (
      <Layout>
        <div className="content">
          <h2>Module not found</h2>
          <Link to="/modules">Back to Modules</Link>
        </div>
      </Layout>
    );
  }

  return (
    <Layout>
      <header className="header">
        <div className="h-title">{module.code} • {module.title}</div>
        <div className="actions">
          <Link className="btn" to="/modules">Back</Link>
          <Link className="btn" to={`/modules/${id}/edit`}>Edit Module</Link>
          <Link className="btn primary" to={`/modules/${id}/assessments/new`}>Add Assessment</Link>
        </div>
      </header>

      <section className="grid two">
        <div className="card">
          <div className="label">Module Lead</div>
          <div className="mt-12">{module.lead}</div>
          <div className="sep"></div>
          <div className="label mt-12">Moderator</div>
          <div className="mt-12">{module.moderator}</div>
          <div className="sep"></div>
          <div className="label mt-12">Staff</div>
          <ul className="mt-12" style={{ display: 'grid', gap: '8px', listStyle: 'none' }}>
            {module.staff.map((s, i) => <li key={i}>{s}</li>)}
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
              {module.assessments.map((a) => (
                <tr key={a.id}>
                  <td>{a.title}</td>
                  <td>{a.type}</td>
                  <td><span className={`badge${a.stageClass ? ' ' + a.stageClass : ''}`}>{a.stage}</span></td>
                  <td><Link to={`/modules/${id}/assessments/${a.id}`}>Open →</Link></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </section>
    </Layout>
  );
}

export default ModuleDetail;

