import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Layout from '../components/Layout';
import { userAPI, assignedUserAPI } from '../services/api';

function MyProfile() {
  const navigate = useNavigate();
  const [isEditing, setIsEditing] = useState(false);
  const [profile, setProfile] = useState(null);
  const [userRoles, setUserRoles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    userType: ''
  });

  useEffect(() => {
    const loadProfile = async () => {
      try {
        setLoading(true);
        const storedUser = localStorage.getItem('currentUser');
        if (!storedUser) {
          navigate('/login');
          return;
        }

        const currentUser = JSON.parse(storedUser);
        const [userRes, rolesRes] = await Promise.all([
          userAPI.getById(currentUser.id),
          assignedUserAPI.getUserRolesById(currentUser.id).catch(() => ({ data: [] }))
        ]);

        const user = userRes.data;
        setProfile(user);
        setFormData({
          username: user.username,
          email: user.email,
          userType: user.userType
        });
        setUserRoles(rolesRes.data || []);
      } catch (err) {
        setError('Failed to load profile');
        console.error('Error loading profile:', err);
      } finally {
        setLoading(false);
      }
    };

    loadProfile();
  }, [navigate]);

  const handleEdit = () => {
    setIsEditing(true);
  };

  const handleCancel = () => {
    setIsEditing(false);
    if (profile) {
      setFormData({
        username: profile.username,
        email: profile.email,
        userType: profile.userType
      });
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const handleSave = async () => {
    if (!profile) return;

    setError('');
    try {
      const updates = [];
      
      if (formData.username !== profile.username) {
        await userAPI.updateUsername(profile.userID, { username: formData.username });
        updates.push('username');
      }
      
      if (formData.email !== profile.email) {
        await userAPI.updateEmail(profile.userID, { email: formData.email });
        updates.push('email');
      }

      // Reload profile
      const userRes = await userAPI.getById(profile.userID);
      setProfile(userRes.data);
      setFormData({
        username: userRes.data.username,
        email: userRes.data.email,
        userType: userRes.data.userType
      });
      
      // Update localStorage
      const storedUser = JSON.parse(localStorage.getItem('currentUser'));
      localStorage.setItem('currentUser', JSON.stringify({
        ...storedUser,
        username: userRes.data.username,
        email: userRes.data.email
      }));

      setIsEditing(false);
      alert('Profile updated successfully!');
    } catch (err) {
      setError('Failed to update profile');
      console.error('Error updating profile:', err);
    }
  };

  const getUserTypeLabel = (userType) => {
    if (!userType) return 'N/A';
    return userType.replace(/_/g, ' ').toLowerCase()
      .replace(/\b\w/g, l => l.toUpperCase());
  };

  if (loading) {
    return (
      <Layout>
        <div className="card">
          <p>Loading profile...</p>
        </div>
      </Layout>
    );
  }

  if (!profile) {
    return (
      <Layout>
        <div className="card">
          <p>Profile not found. Please log in again.</p>
        </div>
      </Layout>
    );
  }

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

            {/* Username */}
            <div className="form-group">
              <label htmlFor="username">Username</label>
              <input
                type="text"
                id="username"
                name="username"
                value={formData.username}
                onChange={handleChange}
                className="input"
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
                className="input"
              />
            </div>

            {/* User Type (read-only) */}
            <div className="form-group">
              <label htmlFor="userType">User Type</label>
              <input
                type="text"
                id="userType"
                name="userType"
                value={getUserTypeLabel(formData.userType)}
                disabled
                className="input"
                style={{ opacity: 0.6 }}
              />
              <small style={{ color: '#666' }}>User type cannot be changed</small>
            </div>

            {error && (
              <div style={{ color: 'red', marginTop: '12px' }}>{error}</div>
            )}
            {/* Form actions: Save + Cancel */}
            <div className="form-actions" style={{ marginTop: '24px' }}>
              <button onClick={handleSave} className="btn primary">
                Save Changes
              </button>
              <button onClick={handleCancel} className="btn" style={{ marginLeft: '12px' }}>
                Cancel
              </button>
            </div>
          </div>

        ) : (

          /* VIEW MODE (not editing) — shows profile info in table format */
          <div className="card" style={{ marginTop: '24px' }}>
            <h2>Personal Information</h2>
            <table className="table" style={{ marginTop: '16px' }}>
              <tbody>
                <tr>
                  <td><strong>Username</strong></td>
                  <td>{profile.username}</td>
                </tr>
                <tr>
                  <td><strong>Email</strong></td>
                  <td>{profile.email}</td>
                </tr>
                <tr>
                  <td><strong>User Type</strong></td>
                  <td>{getUserTypeLabel(profile.userType)}</td>
                </tr>
                <tr>
                  <td><strong>User ID</strong></td>
                  <td>{profile.userID}</td>
                </tr>
                <tr>
                  <td><strong>Roles</strong></td>
                  <td>
                    {userRoles.length > 0 ? (
                      userRoles.map((role, i) => (
                        <span key={i} className="pill" style={{ marginRight: '8px' }}>
                          {role}
                        </span>
                      ))
                    ) : (
                      'No roles assigned'
                    )}
                  </td>
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
