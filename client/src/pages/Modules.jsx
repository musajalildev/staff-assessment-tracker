import { Link } from 'react-router-dom';
import Layout from '../components/Layout';

function Modules() {
  return (
    <Layout>
      <header className="header">
        <div className="h-title">Modules</div>
        <div className="actions">
          <button className="btn">Upload CSV</button>
          <Link className="btn primary" to="/modules/new">Add Module</Link>
        </div>
      </header>

      <section className="card">
        <table className="table">
          <thead>
            <tr>
              <th>Code</th>
              <th>Title</th>
              <th>Lead</th>
              <th>Moderator</th>
              <th>Assessments</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>CSC101</td>
              <td>Programming 1</td>
              <td>Jane Doe</td>
              <td>Anna Lee</td>
              <td>3</td>
              <td><Link to="/modules/1">Open →</Link></td>
            </tr>
            <tr>
              <td>CSC212</td>
              <td>Data Structures</td>
              <td>Juan Park</td>
              <td>Jane Doe</td>
              <td>2</td>
              <td><Link to="/modules/2">Open →</Link></td>
            </tr>
            <tr>
              <td>MAT150</td>
              <td>Discrete Math</td>
              <td>John Smith</td>
              <td>Anna Lee</td>
              <td>2</td>
              <td><Link to="/modules/3">Open →</Link></td>
            </tr>
          </tbody>
        </table>
      </section>
    </Layout>
  );
}

export default Modules;

