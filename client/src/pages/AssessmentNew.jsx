import { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import Layout from '../components/Layout';

function AssessmentNew() {
  const { moduleId } = useParams();
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    title: '',
    type: 'Coursework',
    setter: '',
    checker: '',
    weight: '',
    due: '',
    notes: '',
    markedByTeam: false
  });

  const handleChange = (e) => {
    const value = e.target.type === 'checkbox' ? e.target.checked : e.target.value;
    setFormData({
      ...formData,
      [e.target.name]: value
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // TODO: Implement API call to create assessment
    console.log('Creating assessment:', formData);
    navigate(`/modules/${moduleId}`);
  };

  return (
    <Layout>
      <header className="header">
        <div className="h-title">Add Assessment (CSC101)</div>
        <div className="actions">
          <button className="btn" onClick={() => navigate(`/modules/${moduleId}`)}>Cancel</button>
          <button className="btn primary" onClick={handleSubmit}>Save</button>
        </div>
      </header>

      <section className="card">
        <form className="form" onSubmit={handleSubmit}>
          <div className="grid two">
            <div className="field">
              <label className="label" htmlFor="a-title">Title</label>
              <input
                className="input"
                id="a-title"
                name="title"
                placeholder="Assignment 1"
                value={formData.title}
                onChange={handleChange}
              />
            </div>
            <div className="field">
              <label className="label" htmlFor="a-type">Type</label>
              <select
                className="select"
                id="a-type"
                name="type"
                value={formData.type}
                onChange={handleChange}
              >
                <option>Coursework</option>
                <option>Test</option>
                <option>Exam</option>
              </select>
            </div>
          </div>
          <div className="grid two">
            <div className="field">
              <label className="label" htmlFor="setter">Setter</label>
              <select
                className="select"
                id="setter"
                name="setter"
                value={formData.setter}
                onChange={handleChange}
              >
                <option value="">Select...</option>
                <option value="1">John Smith</option>
              </select>
            </div>
            <div className="field">
              <label className="label" htmlFor="checker">Checker (override)</label>
              <select
                className="select"
                id="checker"
                name="checker"
                value={formData.checker}
                onChange={handleChange}
              >
                <option value="">— Use module default —</option>
                <option value="1">Anna Lee</option>
              </select>
            </div>
          </div>
          <div className="grid two">
            <div className="field">
              <label className="label" htmlFor="weight">Weight (%)</label>
              <input
                className="input"
                id="weight"
                name="weight"
                type="number"
                min="0"
                max="100"
                placeholder="20"
                value={formData.weight}
                onChange={handleChange}
              />
            </div>
            <div className="field">
              <label className="label" htmlFor="due">Due Date</label>
              <input
                className="input"
                id="due"
                name="due"
                type="date"
                value={formData.due}
                onChange={handleChange}
              />
            </div>
          </div>
          <div className="field">
            <label className="label" htmlFor="notes">Notes</label>
            <textarea
              className="textarea"
              id="notes"
              name="notes"
              placeholder="Any special instructions…"
              value={formData.notes}
              onChange={handleChange}
            ></textarea>
          </div>
          <div className="field">
            <label className="label">Marked by Team?</label>
            <div>
              <label>
                <input
                  type="checkbox"
                  name="markedByTeam"
                  checked={formData.markedByTeam}
                  onChange={handleChange}
                />{' '}
                Yes
              </label>
            </div>
          </div>
        </form>
      </section>
    </Layout>
  );
}

export default AssessmentNew;

