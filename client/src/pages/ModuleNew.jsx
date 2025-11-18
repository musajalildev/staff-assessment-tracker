import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Layout from '../components/Layout';

function ModuleNew() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    code: '',
    title: '',
    lead: '',
    moderator: '',
    staff: ''
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // TODO: Implement API call to create module
    console.log('Creating module:', formData);
    navigate('/modules');
  };

  return (
    <Layout>
      <header className="header">
        <div className="h-title">Add Module</div>
        <div className="actions">
          <button className="btn" onClick={() => navigate('/modules')}>Cancel</button>
          <button className="btn primary" onClick={handleSubmit}>Save</button>
        </div>
      </header>

      <section className="card">
        <form className="form" onSubmit={handleSubmit}>
          <div className="grid two">
            <div className="field">
              <label className="label" htmlFor="code">Module Code</label>
              <input
                className="input"
                id="code"
                name="code"
                placeholder="e.g., CSC101"
                value={formData.code}
                onChange={handleChange}
              />
            </div>
            <div className="field">
              <label className="label" htmlFor="title">Title</label>
              <input
                className="input"
                id="title"
                name="title"
                placeholder="Programming 1"
                value={formData.title}
                onChange={handleChange}
              />
            </div>
          </div>
          <div className="grid two">
            <div className="field">
              <label className="label" htmlFor="lead">Module Lead</label>
              <select
                className="select"
                id="lead"
                name="lead"
                value={formData.lead}
                onChange={handleChange}
              >
                <option value="">Select...</option>
                <option value="1">Jane Doe</option>
                <option value="2">John Smith</option>
              </select>
            </div>
            <div className="field">
              <label className="label" htmlFor="moderator">Moderator</label>
              <select
                className="select"
                id="moderator"
                name="moderator"
                value={formData.moderator}
                onChange={handleChange}
              >
                <option value="">Select...</option>
                <option value="1">Anna Lee</option>
                <option value="2">Mary Chan</option>
              </select>
            </div>
          </div>
          <div className="field">
            <label className="label" htmlFor="staff">Other Staff (comma separated)</label>
            <input
              className="input"
              id="staff"
              name="staff"
              placeholder="John Smith, Mary Chan"
              value={formData.staff}
              onChange={handleChange}
            />
          </div>
        </form>
      </section>
    </Layout>
  );
}

export default ModuleNew;

