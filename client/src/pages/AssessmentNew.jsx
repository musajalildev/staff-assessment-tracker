import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import Layout from '../components/Layout';
import { assessmentAPI, userAPI, moduleAPI } from '../services/api';

function AssessmentNew() {
  const { moduleId } = useParams();
  const navigate = useNavigate();
  const [users, setUsers] = useState([]);
  const [module, setModule] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [formData, setFormData] = useState({
    title: '',
    type: 'COURSEWORK',
    setter: '',
    checker: '',
    progress: 'CREATED',
    teamMarked: false,
    autoGraded: false,
    examDate: '',
    deadlineDate: ''
  });

  useEffect(() => {
    const loadData = async () => {
      try {
        const usersRes = await userAPI.getAll();
        setUsers(usersRes.data || []);
        
        // Try to load module info
        try {
          const moduleRes = await moduleAPI.getByCode(moduleId);
          setModule(moduleRes.data);
        } catch (e) {
          const allModulesRes = await moduleAPI.getAll();
          const foundModule = (allModulesRes.data || []).find(
            m => (m.id || m.ID)?.toString() === moduleId
          );
          if (foundModule) setModule(foundModule);
        }
      } catch (err) {
        console.error('Error loading data:', err);
      }
    };
    loadData();
  }, [moduleId]);

  const handleChange = (e) => {
    const value = e.target.type === 'checkbox' ? e.target.checked : e.target.value;
    setFormData({
      ...formData,
      [e.target.name]: value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const assessmentData = {
        title: formData.title,
        type: formData.type,
        progress: formData.progress,
        teamMarked: formData.teamMarked,
        autoGraded: formData.autoGraded,
        examDate: formData.examDate || null,
        deadlineDate: formData.deadlineDate || null,
        setter: formData.setter ? { userID: formData.setter } : null,
        checker: formData.checker ? { userID: formData.checker } : null,
        module: module ? { id: module.id || module.ID } : null
      };

      await assessmentAPI.create(assessmentData);
      navigate(`/modules/${moduleId}`);
    } catch (err) {
      setError('Failed to create assessment. Please try again.');
      console.error('Error creating assessment:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Layout>
      <header className="header">
        <div className="h-title">Add Assessment {module ? `(${module.code || module.moduleCode})` : ''}</div>
        <div className="actions">
          <button className="btn" onClick={() => navigate(`/modules/${moduleId}`)}>Cancel</button>
          <button className="btn primary" onClick={handleSubmit} disabled={loading || !formData.title}>
            {loading ? 'Creating...' : 'Save'}
          </button>
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
                onChange={(e) => {
                  setFormData({ 
                    ...formData, 
                    type: e.target.value,
                    autoGraded: e.target.value === 'TEST_AUTOGRADED' ? true : formData.autoGraded && (e.target.value.includes('TEST'))
                  });
                }}
              >
                <option value="COURSEWORK">Coursework</option>
                <option value="TEST_AUTOGRADED">Test (Autograded)</option>
                <option value="TEST_SINGLE_MARKER">Test (Single Marker)</option>
                <option value="TEST_TEAM_MARKER">Test (Team Marker)</option>
                <option value="EXAM">Exam</option>
              </select>
            </div>
          </div>
          <div className="grid two">
            <div className="field">
              <label className="label" htmlFor="setter">Setter (Optional)</label>
              <select
                className="select"
                id="setter"
                name="setter"
                value={formData.setter}
                onChange={handleChange}
              >
                <option value="">Select...</option>
                {users.map((user) => (
                  <option key={user.userID} value={user.userID}>
                    {user.username} ({user.email})
                  </option>
                ))}
              </select>
            </div>
            <div className="field">
              <label className="label" htmlFor="checker">Checker (Optional)</label>
              <select
                className="select"
                id="checker"
                name="checker"
                value={formData.checker}
                onChange={handleChange}
              >
                <option value="">— Use module default —</option>
                {users.map((user) => (
                  <option key={user.userID} value={user.userID}>
                    {user.username} ({user.email})
                  </option>
                ))}
              </select>
            </div>
          </div>
          <div className="grid two">
            <div className="field">
              <label className="label" htmlFor="deadlineDate">
                {formData.type === 'COURSEWORK' ? 'Submission Deadline' : formData.type === 'EXAM' ? 'Exam Date' : 'Test Date'}
              </label>
              <input
                type="date"
                className="input"
                id="deadlineDate"
                name={formData.type === 'EXAM' ? 'examDate' : 'deadlineDate'}
                value={formData.type === 'EXAM' ? formData.examDate : formData.deadlineDate}
                onChange={(e) => {
                  if (formData.type === 'EXAM') {
                    setFormData({ ...formData, examDate: e.target.value });
                  } else {
                    setFormData({ ...formData, deadlineDate: e.target.value });
                  }
                }}
              />
              <p className="sub mt-8" style={{ fontSize: '12px' }}>
                {formData.type === 'EXAM' || formData.type.includes('TEST') 
                  ? 'Assessment will automatically progress the next working day after this date'
                  : 'Deadline for student submissions'}
              </p>
            </div>
            <div className="field">
              <label className="label">Options</label>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', marginTop: '8px' }}>
                <label>
                  <input
                    type="checkbox"
                    name="teamMarked"
                    checked={formData.teamMarked}
                    onChange={handleChange}
                  />{' '}
                  Marked by Team
                </label>
                <label>
                  <input
                    type="checkbox"
                    name="autoGraded"
                    checked={formData.autoGraded}
                    onChange={handleChange}
                    disabled={formData.type === 'COURSEWORK' || formData.type === 'EXAM'}
                  />{' '}
                  Auto Graded
                  {(formData.type === 'COURSEWORK' || formData.type === 'EXAM') && (
                    <span className="sub" style={{ fontSize: '11px', marginLeft: '4px' }}>
                      (Tests only)
                    </span>
                  )}
                </label>
              </div>
            </div>
          </div>
          {error && (
            <div style={{ color: 'red', marginTop: '12px' }}>{error}</div>
          )}
        </form>
      </section>
    </Layout>
  );
}

export default AssessmentNew;

