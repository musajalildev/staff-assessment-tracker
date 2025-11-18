import { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import Layout from '../components/Layout';

function ModuleEdit() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    code: 'CSC101',
    title: 'Programming 1',
    lead: '1',
    moderator: '1',
    staff: 'John Smith, Mary Chan'
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // TODO: Implement API call to update module
    console.log('Updating module:', id, formData);
    navigate(`/modules/${id}`);
  };

  return (
    <Layout>
      <header className="header">
        <div className="h-title">Edit Module (CSC101)</div>
        <div className="actions">
          <button className="btn" onClick={() => navigate(`/modules/${id}`)}>Cancel</button>
          <button className="btn primary" onClick={handleSubmit}>Save</button>
        </div>
      </header>

      <section className="card">
        <form className="form" onSubmit={handleSubmit}>
          <div className="grid two">
            <div className="field">
              <label className="label" htmlFor="code2">Module Code</label>
              <input
                className="input"
                id="code2"
                name="code"
                value={formData.code}
                onChange={handleChange}
              />
            </div>
            <div className="field">
              <label className="label" htmlFor="title2">Title</label>
              <input
                className="input"
                id="title2"
                name="title"
                value={formData.title}
                onChange={handleChange}
              />
            </div>
          </div>
          <div className="grid two">
            <div className="field">
              <label className="label" htmlFor="lead2">Module Lead</label>
              <select
                className="select"
                id="lead2"
                name="lead"
                value={formData.lead}
                onChange={handleChange}
              >
                <option value="1">Jane Doe</option>
                <option value="2">John Smith</option>
              </select>
            </div>
            <div className="field">
              <label className="label" htmlFor="moderator2">Moderator</label>
              <select
                className="select"
                id="moderator2"
                name="moderator"
                value={formData.moderator}
                onChange={handleChange}
              >
                <option value="1">Anna Lee</option>
                <option value="2">Mary Chan</option>
              </select>
            </div>
          </div>
          <div className="field">
            <label className="label" htmlFor="staff2">Other Staff</label>
            <input
              className="input"
              id="staff2"
              name="staff"
              value={formData.staff}
              onChange={handleChange}
            />
          </div>
        </form>
      </section>
    </Layout>
  );
}

export default ModuleEdit;

