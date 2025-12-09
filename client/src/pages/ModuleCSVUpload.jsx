import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Layout from '../components/Layout';
import { moduleAPI } from '../services/api';

function ModuleCSVUpload() {
  const navigate = useNavigate();
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  const handleFileChange = (e) => {
    const selectedFile = e.target.files[0];
    if (selectedFile) {
      if (selectedFile.type === 'text/csv' || selectedFile.name.endsWith('.csv')) {
        setFile(selectedFile);
        setError('');
      } else {
        setError('Please select a CSV file');
        setFile(null);
      }
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!file) {
      setError('Please select a CSV file');
      return;
    }

    setLoading(true);
    setError('');
    setSuccess(false);

    try {
      const reader = new FileReader();
      reader.onload = async (event) => {
        try {
          const csvData = event.target.result;
          await moduleAPI.createFromCSV(csvData);
          setSuccess(true);
          setTimeout(() => {
            navigate('/modules');
          }, 2000);
        } catch (err) {
          setError('Failed to upload CSV. Please check the format and try again.');
          console.error('Error uploading CSV:', err);
        } finally {
          setLoading(false);
        }
      };
      reader.readAsText(file);
    } catch (err) {
      setError('Failed to read file. Please try again.');
      console.error('Error reading file:', err);
      setLoading(false);
    }
  };

  return (
    <Layout>
      <header className="header">
        <div className="h-title">Upload Modules from CSV</div>
        <div className="actions">
          <button className="btn" onClick={() => navigate('/modules')}>Cancel</button>
        </div>
      </header>

      <section className="card">
        <div style={{ marginBottom: '24px' }}>
          <h3 style={{ marginBottom: '12px' }}>CSV Format</h3>
          <p className="sub" style={{ marginBottom: '8px' }}>
            Your CSV file should have the following columns:
          </p>
          <ul style={{ marginLeft: '24px', marginBottom: '16px' }}>
            <li><strong>Module Code</strong> (e.g., COM2008)</li>
            <li><strong>Title</strong> (e.g., Software Engineering)</li>
            <li><strong>Module Lead</strong> (username or email)</li>
            <li><strong>Moderator</strong> (username or email, optional)</li>
          </ul>
          <p className="sub" style={{ fontSize: '12px', color: '#888' }}>
            Extended format: You can also include a Moderator column. The first row should be headers.
          </p>
        </div>

        <form className="form" onSubmit={handleSubmit}>
          <div className="field">
            <label className="label" htmlFor="csvFile">Select CSV File</label>
            <input
              type="file"
              id="csvFile"
              accept=".csv,text/csv"
              onChange={handleFileChange}
              style={{
                padding: '8px',
                border: '1px solid var(--border)',
                borderRadius: '4px',
                width: '100%',
                background: 'var(--bg)',
                color: 'var(--text)'
              }}
            />
            {file && (
              <p className="sub mt-8" style={{ fontSize: '14px' }}>
                Selected: {file.name} ({(file.size / 1024).toFixed(2)} KB)
              </p>
            )}
          </div>

          {error && (
            <div style={{ 
              background: 'rgba(255, 51, 102, 0.1)', 
              border: '1px solid var(--bad)', 
              color: 'var(--bad)', 
              padding: '12px', 
              borderRadius: '4px',
              marginTop: '16px'
            }}>
              {error}
            </div>
          )}

          {success && (
            <div style={{ 
              background: 'rgba(0, 255, 0, 0.1)', 
              border: '1px solid #0f0', 
              color: '#0f0', 
              padding: '12px', 
              borderRadius: '4px',
              marginTop: '16px'
            }}>
              CSV uploaded successfully! Redirecting...
            </div>
          )}

          <div className="field" style={{ marginTop: '24px' }}>
            <button 
              className="btn primary" 
              type="submit" 
              disabled={loading || !file || success}
            >
              {loading ? 'Uploading...' : 'Upload CSV'}
            </button>
            <button 
              className="btn" 
              type="button"
              onClick={() => navigate('/modules')}
              style={{ marginLeft: '12px' }}
            >
              Cancel
            </button>
          </div>
        </form>
      </section>
    </Layout>
  );
}

export default ModuleCSVUpload;

