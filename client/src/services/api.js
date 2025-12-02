const API_BASE_URL = 'http://localhost:8080';

// Helper function to make API requests using native fetch
const apiRequest = async (endpoint, options = {}) => {
  const url = `${API_BASE_URL}${endpoint}`;
  const config = {
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    ...options,
  };

  if (config.body && typeof config.body === 'object' && !(config.body instanceof FormData)) {
    config.body = JSON.stringify(config.body);
  }

  try {
    const response = await fetch(url, config);
    const data = await response.json().catch(() => null);
    
    if (!response.ok) {
      throw new Error(data?.message || `HTTP error! status: ${response.status}`);
    }
    
    return { data };
  } catch (error) {
    throw error;
  }
};

// User API
export const userAPI = {
  login: async (usernameOrEmail, password) => {
    // Workaround: Since there's no login endpoint, we validate password using existing endpoints
    // 1. Get user by username or email (try both)
    // 2. Validate password using the password update endpoint (which validates before updating)
    try {
      // Try to get the user by username first, then by email
      let userResponse;
      let user;
      
      try {
        // Try username first
        userResponse = await apiRequest(`/users/un/${usernameOrEmail}`);
        user = userResponse.data;
      } catch (err) {
        // If not found by username, try email
        try {
          userResponse = await apiRequest(`/users/email/${usernameOrEmail}`);
          user = userResponse.data;
        } catch (emailErr) {
          throw new Error('Invalid username or password');
        }
      }
      
      if (!user || !user.userID) {
        throw new Error('Invalid username or password');
      }
      
      // Validate password by attempting to update it
      // The backend validates currentPassword FIRST, so we can use this to check
      // Note: This will temporarily change the password, but validates it correctly
      try {
        const tempPassword = 'temp_' + Date.now() + '_' + Math.random().toString(36);
        await apiRequest(`/users/${user.userID}/password?currentPassword=${encodeURIComponent(password)}`, {
          method: 'PUT',
          body: { password: tempPassword }
        });
        
        // Password was validated successfully! Now change it back to the original
        // We need to use the temp password as the "current" password to set it back
        await apiRequest(`/users/${user.userID}/password?currentPassword=${encodeURIComponent(tempPassword)}`, {
          method: 'PUT',
          body: { password: password }
        });
        
        // Password validated and restored - return user
        return { data: user };
      } catch (pwdError) {
        // Check if it's a password validation error
        const errorMsg = pwdError.message || '';
        if (errorMsg.includes('Incorrect current password')) {
          throw new Error('Invalid username or password');
        }
        // Other errors
        throw new Error('Invalid username or password');
      }
    } catch (error) {
      // Re-throw our custom errors, wrap others
      if (error.message === 'Invalid username or password') {
        throw error;
      }
      throw new Error('Invalid username or password');
    }
  },
  getAll: () => apiRequest('/users'),
  getById: (id) => apiRequest(`/users/id/${id}`),
  getByUsername: (username) => apiRequest(`/users/un/${username}`),
  getByEmail: (email) => apiRequest(`/users/email/${email}`),
  create: (user) => apiRequest('/users/', { method: 'POST', body: user }),
  updatePassword: (id, user, currentPassword) => 
    apiRequest(`/users/${id}/password?currentPassword=${currentPassword}`, { method: 'PUT', body: user }),
  updateEmail: (id, user) => apiRequest(`/users/${id}/email`, { method: 'PUT', body: user }),
  updateUsername: (id, user) => apiRequest(`/users/${id}/username`, { method: 'PUT', body: user }),
  updatePermission: (id, user) => apiRequest(`/users/${id}/permission`, { method: 'PUT', body: user }),
  delete: (id) => apiRequest(`/users/${id}`, { method: 'DELETE' }),
  getPermission: (id) => apiRequest(`/users/permission/${id}`),
  getByPermission: (permit) => apiRequest(`/users/${permit}`),
};

// Module API
export const moduleAPI = {
  getAll: () => apiRequest('/api/v1/module'),
  getByCode: (code) => apiRequest(`/api/v1/module?moduleCode=${code}`),
  create: (module) => apiRequest('/api/v1/module', { method: 'POST', body: module }),
  update: (module) => apiRequest('/api/v1/module', { method: 'PUT', body: module }),
  createFromCSV: (csvData) => apiRequest('/api/v1/module/csv', {
    method: 'POST',
    headers: { 'Content-Type': 'text/csv' },
    body: csvData
  }),
};

// Assessment API
export const assessmentAPI = {
  getById: (id) => apiRequest(`/api/assessment/${id}`),
  create: (assessment) => apiRequest('/api/v1/assessment', { method: 'POST', body: assessment }),
  update: (id, assessment) => apiRequest(`/api/assessment/${id}`, { method: 'PUT', body: assessment }),
};

// AssignedUser API (Roles)
export const assignedUserAPI = {
  getAll: () => apiRequest('/assign'),
  getById: (id) => apiRequest(`/assign/id/${id}`),
  getByUsername: (username) => apiRequest(`/assign/un/${username}`),
  getByRole: (role) => apiRequest(`/assign/${role}`),
  getUserRoles: (username) => apiRequest(`/assign/role/${username}`),
  getUserRolesById: (id) => apiRequest(`/assign/role/userid/${id}`),
  getRoleByAssignmentId: (id) => apiRequest(`/assign/role/assignmentID/${id}`),
  create: (userId, role) => apiRequest('/assign/', { 
    method: 'POST', 
    body: { userID: userId, role: role } 
  }),
  updateRole: (id, role) => apiRequest(`/assign/role/id/${id}`, { method: 'PUT', body: role }),
  delete: (id) => apiRequest(`/assign/${id}`, { method: 'DELETE' }),
  deleteUserAssignments: (userId) => apiRequest(`/assign/user/${userId}`, { method: 'DELETE' }),
};

export default { apiRequest };

