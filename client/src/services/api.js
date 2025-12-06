const API_BASE_URL = 'http://localhost:8080';

// Helper function to make API requests using native fetch
const apiRequest = async (endpoint, options = {}) => {
  const url = `${API_BASE_URL}${endpoint}`;

  // Get auth token from localStorage if available
  const authToken = localStorage.getItem('authToken');

  const config = {
    headers: {
      'Content-Type': 'application/json',
      ...(authToken && { 'Authorization': `Bearer ${authToken}` }),
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
    // Remove any existing token before attempting login
    localStorage.removeItem('authToken');

    try {
      // Call the /auth/login endpoint with LoginDTO
      // LoginDTO has: { identifier: string, password: string }
      const loginResponse = await apiRequest('/auth/login', {
        method: 'POST',
        body: {
          identifier: usernameOrEmail,
          password: password
        }
      });

      // The response contains a TokenDTO with a token
      const token = loginResponse.data?.token;
      if (!token) {
        throw new Error('Invalid username or password, Token not generated');
      }

      // Store the token for future authenticated requests
      localStorage.setItem('authToken', token);

      // Fetch user info using the identifier
      // If identifier contains "@", try email first; otherwise try username first
      let user;
      const isEmail = usernameOrEmail.includes('@');

      try {
        if (isEmail) {
          // Try email first if identifier looks like an email
          const userResponse = await apiRequest(`/users/email/${usernameOrEmail}`);
          user = userResponse.data;
        } else {
          // Try username first if identifier doesn't look like an email
          const userResponse = await apiRequest(`/users/un/${usernameOrEmail}`);
          user = userResponse.data;
        }
      } catch (err) {
        // If first attempt failed, try the other endpoint
        try {
          if (isEmail) {
            // Try username if email failed
            const userResponse = await apiRequest(`/users/un/${usernameOrEmail}`);
            user = userResponse.data;
          } else {
            // Try email if username failed
            const userResponse = await apiRequest(`/users/email/${usernameOrEmail}`);
            user = userResponse.data;
          }
        } catch (secondErr) {
          throw new Error('Failed to fetch user Information');
        }
      }

      if (!user || !user.userID) {
        throw new Error('User Info Invalid');
      }

      return { data: user };
    } catch (error) {
      // Remove token if login failed
      localStorage.removeItem('authToken');

      // Re-throw our custom errors
      if (error.message === 'Invalid username or password' || error.message === 'Failed to fetch user information') {
        throw error;
      }
      // Check if it's an authentication error from the backend
      if (error.message && (
        error.message.includes('401') ||
        error.message.includes('403') ||
        error.message.includes('Unauthorized') ||
        error.message.includes('Bad credentials'))) {
        throw new Error('Backend Malfunction');
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
  update: (id, module) => apiRequest('/api/v1/module', { method: 'PUT', body: module }),
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
`/api/assessment/${id}
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

// Feedback API
export const feedbackAPI = {
  // Get all feedback for an assessment
  getByAssessment: (assessmentId) => apiRequest(`/assessments/${assessmentId}/feedback`),

  // Get specific feedback by ID
  getById: (feedbackId) => apiRequest(`/assessments/feedback/${feedbackId}`),

  // Create new feedback
  create: (assessmentId, feedback) => apiRequest(`/assessments/${assessmentId}/feedback`, {
    method: 'POST',
    body: feedback
  }),

  // Update feedback
  update: (feedbackId, feedback) => apiRequest(`/assessments/feedback/${feedbackId}`, {
    method: 'PUT',
    body: feedback
  }),

  // Delete feedback
  delete: (feedbackId) => apiRequest(`/assessments/feedback/${feedbackId}`, {
    method: 'DELETE'
  }),
};

export default { apiRequest };

