import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Layout from '../components/Layout';

function MyProfile() {
  const navigate = useNavigate();

  // Controls whether the profile is in "edit" mode or "view" mode
  const [isEditing, setIsEditing] = useState(false);
  
  // Mock user data (temporary) — this would come from backend API
  const [profile, setProfile] = useState({
    id: 1,
    name: 'John Doe',
    email: 'john.doe@example.com',
    role: 'Admin',
    department: 'Engineering',
    joinDate: '2024-01-15'
  });

  // Stores the editable form data when the user is editing
  const [formData, setFormData] = useState(profile);

  // Activate editing mode and load current profile data into the form
  const handleEdit = () => {
    setIsEditing(true);
    setFormData(profile);
  };

  // Cancel editing and return to view mode without saving
  const handleCancel = () => {
    setIsEditing(false);
  };

  // Update form values as the user types
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  // Save profile changes (currently only updates local state)
  // TODO: Replace with API call to save to server
  const handleSave = () => {
    setProfile(formData);
    setIsEditing(false);
    console.log('Profile updated:', formData);
  };

  return (
    // Layout wrapper: gives navbar/sidebar consistent across all pages
    <Layout>
      <div className="profile-container">
        
        {/* Page header with title + Edit button */}
        <div className="profile-header">
          <h1>My Profile</h1>

          {/* Show Edit button only when NOT editing */}
          {!isEditing && (
            <button onClick={handleEdit} className="btn btn-primary">
              Edit Profile
            </button>
          )}
        </div>

        {/* If user is editing, show editable input fields */}
        {isEditing ? (
          <div className="profile-form">

            {/* Name */}
            <div className="form-group">
              <label htmlFor="name">Name</label>
              <input
                type="text"
                id="name"
                name="name"
                value={formData.name}
                onChange={handleChange}
              />
            </div>

            {/* Email */}
            <div className="form-group">
              <label htmlFor="email">Email</label>
              <input
                type="email"
                id="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
              />
            </div>

            {/* Department */}
            <div className="form-group">
              <label htmlFor="department">Department</label>
              <input
                type="text"
                id="department"
                name="department"
                value={formData.department}
                onChange={handleChange}
              />
            </div>

            {/* Form actions: Save + Cancel */}
            <div className="form-actions">
              <button onClick={handleSave} className="btn btn-success">
                Save Changes
              </button>
              <button onClick={handleCancel} className="btn btn-secondary">
                Cancel
              </button>
            </div>
          </div>

        ) : (

          /* VIEW MODE (not editing) — shows profile info in table format */
          <div className="card" style={{ marginTop: '80px' }}>
            <h2>Personal Information</h2>
            <table className="table" style={{ marginTop: '16px' }}>
              <tbody>
                <tr>
                  <td><strong>Name</strong></td>
                  <td>{profile.name}</td>
                </tr>
                <tr>
                  <td><strong>Email</strong></td>
                  <td>{profile.email}</td>
                </tr>
                <tr>
                  <td><strong>Role</strong></td>
                  <td>{profile.role}</td>
                </tr>
                <tr>
                  <td><strong>Department</strong></td>
                  <td>{profile.department}</td>
                </tr>
                <tr>
                  <td><strong>Join Date</strong></td>
                  <td>{new Date(profile.joinDate).toLocaleDateString()}</td>
                </tr>
              </tbody>
            </table>
          </div>
        )}
      </div>
    </Layout>
  );
}

export default MyProfile;
