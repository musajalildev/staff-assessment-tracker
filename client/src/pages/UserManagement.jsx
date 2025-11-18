import Layout from '../components/Layout';

function UserManagement() {
  return (
    <Layout>
      <header className="header">
        <div className="h-title">User Management</div>
        <div className="actions">
          <button className="btn primary">Add User</button>
        </div>
      </header>

      <section className="card">
        <table className="table">
          <thead>
            <tr>
              <th>Name</th>
              <th>Role</th>
              <th>Exam Officer?</th>
              <th>Modules</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>Jane Doe</td>
              <td>Teaching Support</td>
              <td>No</td>
              <td>All</td>
              <td><a href="#">Edit</a></td>
            </tr>
            <tr>
              <td>John Smith</td>
              <td>Academic</td>
              <td>Yes</td>
              <td>CSC101, MAT150</td>
              <td><a href="#">Edit</a></td>
            </tr>
            <tr>
              <td>Anna Lee</td>
              <td>Academic</td>
              <td>No</td>
              <td>CSC101</td>
              <td><a href="#">Edit</a></td>
            </tr>
            <tr>
              <td>Mary Chan</td>
              <td>External Examiner</td>
              <td>—</td>
              <td>UG Modules</td>
              <td><a href="#">Edit</a></td>
            </tr>
          </tbody>
        </table>
      </section>
    </Layout>
  );
}

export default UserManagement;

