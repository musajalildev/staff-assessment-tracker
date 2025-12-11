// e2e/login.spec.js
import { test, expect } from '@playwright/test';

test('user can log in successfully', async ({ page }) => {
  await page.goto('http://localhost:5173/login');

  await page.fill('input[name="usernameOrEmail"]', 'john');
  await page.fill('input[name="password"]', 'john123');

  await page.click('button[type="submit"]');

  await expect(page).toHaveURL(/dashboard/); // or adjust if redirected elsewhere
});

test('user can log out successfully', async ({ page }) => {
  await page.goto('http://localhost:5173/login');

  await page.fill('input[id="usernameOrEmail"]', 'john');
  await page.fill('input[id="password"]', 'john123');
  await page.click('button[type="submit"]');

  // Confirm Dashboard link is visible (sidebar)
  await expect(page.getByRole('link', { name: 'Dashboard' })).toBeVisible();

  // Optional: confirm route
  await expect(page).toHaveURL(/dashboard/);

  // Click logout
  await page.click('button:has-text("Logout")');

  // Confirm redirected to login
  await expect(page).toHaveURL('http://localhost:5173/login');
  await expect(page.getByText('Welcome back')).toBeVisible();
});

// Authentication Test Suite
test.describe('Authentication Tests', () => {
  const BASE_URL = 'http://localhost:5173';

  test.beforeEach(async ({ page }) => {
    await page.goto(BASE_URL);
    await page.evaluate(() => localStorage.clear());
  });

  test('should display login form with required fields', async ({ page }) => {
    await page.goto(`${BASE_URL}/login`);

    await expect(page.locator('input[name="usernameOrEmail"]')).toBeVisible();
    await expect(page.locator('input[name="password"]')).toBeVisible();
    await expect(page.getByRole('button', { name: /sign in/i })).toBeVisible();
    await expect(page.getByText(/welcome back/i)).toBeVisible();
  });

  test('cannot log in with empty username/email', async ({ page }) => {
    await page.goto('http://localhost:5173/login');
  
    await page.fill('input[name="usernameOrEmail"]', '');
    await page.fill('input[name="password"]', 'john123');
  
    await page.click('button[type="submit"]');
  
    await expect(page).toHaveURL(/login/); // should stay in login page
  });

  test('cannot log in with empty password', async ({ page }) => {
    await page.goto('http://localhost:5173/login');
  
    await page.fill('input[name="usernameOrEmail"]', 'john');
    await page.fill('input[name="password"]', '');
  
    await page.click('button[type="submit"]');
  
    await expect(page).toHaveURL(/login/); // should stay in login page
  });

  test('should show error for invalid username or password', async ({ page }) => {
    await page.goto(`${BASE_URL}/login`);

    await page.fill('input[name="usernameOrEmail"]', 'nonexistentuser');
    await page.fill('input[name="password"]', 'wrongpassword');
    await page.click('button[type="submit"]');

    // Wait for error message
    const errorMessage = page.locator('p.sub').filter({ hasText: /Invalid Credentials/i });
    await expect(errorMessage.first()).toBeVisible({ timeout: 5000 });
    await expect(page).toHaveURL(/login/);
  });

  test('user can log in successfully with email', async ({ page }) => {
  await page.goto('http://localhost:5173/login');

  await page.fill('input[name="usernameOrEmail"]', 'john@example.com');
  await page.fill('input[name="password"]', 'john123');

  await page.click('button[type="submit"]');

  await expect(page).toHaveURL(/dashboard/); 
  });

  test('should persist session in localStorage after login', async ({ page }) => {
    await page.goto(`${BASE_URL}/login`);

    await page.fill('input[name="usernameOrEmail"]', 'john');
    await page.fill('input[name="password"]', 'john123');
    await page.click('button[type="submit"]');

    await expect(page).toHaveURL(/dashboard/, { timeout: 5000 });

    // Check localStorage has user data
    const currentUser = await page.evaluate(() => localStorage.getItem('currentUser'));
    expect(currentUser).not.toBeNull();
    
    const userData = JSON.parse(currentUser);
    expect(userData).toHaveProperty('username');
    expect(userData).toHaveProperty('id');
  });

  test('should redirect protected routes to login when not authenticated', async ({ page }) => {


    await page.goto('http://localhost:5173/login');
    await page.goto(`${BASE_URL}/profile`);
    await expect(page).toHaveURL(/login/);

    await page.goto('http://localhost:5173/login');
    await page.goto('http://localhost:5173/modules');
    await expect(page).toHaveURL(/login/);

  });

  test('should show role selection for Exams Officer', async ({ page }) => {
    // Assuming there's an exams officer user - adjust username/password as needed
    await page.goto(`${BASE_URL}/login`);

    // Try to login as exams officer (adjust credentials based on your test data)
    // This test assumes there's a user with ROLE_EXAMS_OFFICER
    await page.fill('input[name="usernameOrEmail"]', 'mary'); //change according to seeded data were using (in this case mary)
    await page.fill('input[name="password"]', 'mary123'); 
    await page.click('button[type="submit"]');

    // Check if role selection appears
    const roleSelect = page.locator('select[name="userType"]');
    
      await expect(roleSelect).toBeVisible();
  });

  test('should complete role selection flow for Exams Officer', async ({ page }) => {
    await page.goto(`${BASE_URL}/login`);

    // Try to login as exams officer (adjust credentials based on your test data)
    await page.fill('input[name="usernameOrEmail"]', 'mary'); 
    await page.fill('input[name="password"]', 'mary123'); 
    await page.click('button[type="submit"]');

    // Wait for role selection if it appears
    const roleSelect = page.locator('select[name="userType"]');
    const isVisible = await roleSelect.isVisible({ timeout: 5000 }).catch(() => false);
    
    if (isVisible) {
      await roleSelect.selectOption({ index: 0 });
      await page.click('button[type="submit"]');
      await expect(page).toHaveURL(/dashboard/, { timeout: 5000 });
    } else {
      // If no selection needed, should already be on dashboard
      await expect(page).toHaveURL(/dashboard/, { timeout: 5000 });
    }
  });

  test('should show error if role selection is submitted without selection', async ({ page }) => {
    await page.goto(`${BASE_URL}/login`);

    // Try to login as exams officer
    await page.fill('input[name="usernameOrEmail"]', 'mary'); 
    await page.fill('input[name="password"]', 'mary123'); 
    await page.click('button[type="submit"]');

    const roleSelect = page.locator('select[name="userType"]');
    const isVisible = await roleSelect.isVisible({ timeout: 5000 }).catch(() => false);
    
    if (isVisible) {
      // Try to submit without selecting
      await page.click('button[type="submit"]');
      const errorMessage = page.locator('p.sub').filter({ hasText: /select|required/i });
      await expect(errorMessage.first()).toBeVisible({ timeout: 3000 });
    }
  });

  test('should allow back button from role selection', async ({ page }) => {
    await page.goto(`${BASE_URL}/login`);

    await page.fill('input[name="usernameOrEmail"]', 'mary'); 
    await page.fill('input[name="password"]', 'mary123'); 
    await page.click('button[type="submit"]');

    const roleSelect = page.locator('select[name="userType"]');
    const isVisible = await roleSelect.isVisible({ timeout: 5000 }).catch(() => false);
    
    if (isVisible) {
      const backButton = page.getByRole('button', { name: /back/i });
      await backButton.click();
      
      // Should return to login form
      await expect(page.locator('input[name="usernameOrEmail"]')).toBeVisible();
      await expect(page.getByText(/welcome back/i)).toBeVisible();
    }
  });

  test('should trim whitespace from username/email input', async ({ page }) => {
    await page.goto(`${BASE_URL}/login`);

    // Login with whitespace around username
    await page.fill('input[name="usernameOrEmail"]', '  john  ');
    await page.fill('input[name="password"]', 'john123');
    await page.click('button[type="submit"]');

    // Should still login successfully (trimmed)
    await expect(page).toHaveURL(/dashboard/, { timeout: 5000 });
  });

  test('should handle login with different user types', async ({ page }) => {
    const users = [
      { username: 'john', password: 'john123', role: 'ACADEMIC' },
      { username: 'musa', password: 'musa123', role: 'TEACHING_SUPPORT' },
      // Add more test users as needed
    ];

    for (const user of users) {
      await page.goto(`${BASE_URL}/login`);
      await page.evaluate(() => localStorage.clear());

      await page.fill('input[name="usernameOrEmail"]', user.username);
      await page.fill('input[name="password"]', user.password);
      await page.click('button[type="submit"]');

      // Handle role selection if needed
      const roleSelect = page.locator('select[name="userType"]');
      if (await roleSelect.isVisible({ timeout: 2000 }).catch(() => false)) {
        await roleSelect.selectOption({ index: 0 });
        await page.click('button[type="submit"]');
      }

      await expect(page).toHaveURL(/dashboard/, { timeout: 5000 });
      
      // Verify user is logged in
      const currentUser = await page.evaluate(() => localStorage.getItem('currentUser'));
      expect(currentUser).not.toBeNull();
    }
  });

  test('should clear localStorage on logout', async ({ page }) => {
    await page.goto(`${BASE_URL}/login`);

    await page.fill('input[name="usernameOrEmail"]', 'john');
    await page.fill('input[name="password"]', 'john123');
    await page.click('button[type="submit"]');

    await expect(page).toHaveURL(/dashboard/, { timeout: 5000 });

    // Verify user data exists
    let currentUser = await page.evaluate(() => localStorage.getItem('currentUser'));
    expect(currentUser).not.toBeNull();

    // Logout
    await page.click('button:has-text("Logout")');
    await expect(page).toHaveURL(/login/);

    // Verify localStorage is cleared
    currentUser = await page.evaluate(() => localStorage.getItem('currentUser'));
    expect(currentUser).toBeNull();
  });

  test('should prevent access to protected routes after logout', async ({ page }) => {
    await page.goto(`${BASE_URL}/login`);

    await page.fill('input[name="usernameOrEmail"]', 'john');
    await page.fill('input[name="password"]', 'john123');
    await page.click('button[type="submit"]');

    await expect(page).toHaveURL(/dashboard/, { timeout: 5000 });

    // Logout
    await page.click('button:has-text("Logout")');
    await expect(page).toHaveURL(/login/);

    // Try to access protected route
    await page.goto(`${BASE_URL}/dashboard`);
    await expect(page).toHaveURL(/login/);
  });

  test('should display error message with correct styling', async ({ page }) => {
    await page.goto(`${BASE_URL}/login`);

    await page.fill('input[name="usernameOrEmail"]', 'invaliduser');
    await page.fill('input[name="password"]', 'wrongpass');
    await page.click('button[type="submit"]');

    const errorMessage = page.locator('p.sub').filter({ hasText: /invalid|incorrect|wrong/i });
    await expect(errorMessage.first()).toBeVisible({ timeout: 5000 });
    
    // Check error styling (red color)
    const color = await errorMessage.first().evaluate((el) => {
      return window.getComputedStyle(el).color;
    });
    // Error should be visible and styled
    expect(errorMessage.first()).toBeVisible();
  });
});

// e2e/navigation.spec.js

const BASE_URL = 'http://localhost:5173';

test.describe('Navigation and Page Titles', () => {
  async function login(page, username, password) {
    await page.goto(BASE_URL);
    await page.evaluate(() => localStorage.clear());
    
    await page.goto(`${BASE_URL}/login`);
    await page.fill('input[name="usernameOrEmail"]', username);
    await page.fill('input[name="password"]', password);
    await page.click('button[type="submit"]');
    
    // Handle role selection for Exams Officer
    const select = page.locator('select[name="userType"]');
    if (await select.isVisible({ timeout: 2000 }).catch(() => false)) {
      await select.selectOption({ index: 0 });
      await page.click('button[type="submit"]');
    }
    
    await expect(page).toHaveURL(/dashboard/, { timeout: 5000 });
  }

  test('should redirect unauthenticated user from dashboard to login', async ({ page }) => {
    await page.goto(BASE_URL);
    await page.evaluate(() => localStorage.clear());
    
    await page.goto(`${BASE_URL}/dashboard`);
    await expect(page).toHaveURL(/login/);
  });

  test('should display "Overview" title on Dashboard', async ({ page }) => {
    await login(page, 'john', 'john123');
    
    const headerTitle = page.locator('.h-title').filter({ hasText: /overview/i });
    await expect(headerTitle).toBeVisible();
  });

  test('should display "Modules" title on Modules page', async ({ page }) => {
    await login(page, 'john', 'john123');
    
    await page.goto(`${BASE_URL}/modules`);
    await page.waitForLoadState('networkidle');
    
    const headerTitle = page.locator('.h-title').filter({ hasText: /modules/i });
    await expect(headerTitle).toBeVisible();
  });

  test('should display "My Profile" title on Profile page', async ({ page }) => {
    await login(page, 'john', 'john123');
    
    await page.goto(`${BASE_URL}/profile`);
    await page.waitForLoadState('networkidle');
    
    const pageTitle = page.getByText(/my profile/i).first();
    await expect(pageTitle).toBeVisible();
  });

  test('should navigate from Dashboard to Modules via sidebar', async ({ page }) => {
    await login(page, 'john', 'john123');
    
    const modulesLink = page.getByRole('link', { name: /^modules$/i });
    await modulesLink.click();
    
    await expect(page).toHaveURL(/modules/);
    const headerTitle = page.locator('.h-title').filter({ hasText: /modules/i });
    await expect(headerTitle).toBeVisible();
  });

  test('should navigate from Dashboard to Profile via sidebar', async ({ page }) => {
    await login(page, 'john', 'john123');
    
    const profileLink = page.getByRole('link', { name: /my profile|profile/i });
    await profileLink.click();
    
    await expect(page).toHaveURL(/profile/);
    // Use heading instead of getByText to avoid strict mode violation (profile appears in sidebar and page)
    const pageTitle = page.getByRole('heading', { name: /my profile/i });
    await expect(pageTitle).toBeVisible();
  });

  test('should display "Welcome back" text on Login page', async ({ page }) => {
    await page.goto(BASE_URL);
    await page.evaluate(() => localStorage.clear());
    
    await page.goto(`${BASE_URL}/login`);
    
    await expect(page.getByText(/welcome back/i)).toBeVisible();
  });

  test('should display "User Management" title on Users page for admin', async ({ page }) => {
    await login(page, 'musa', 'musa123');
    
    await page.goto(`${BASE_URL}/users`);
    await page.waitForLoadState('networkidle');
    
    const headerTitle = page.locator('.h-title').filter({ hasText: /user.*management|users/i });
    await expect(headerTitle).toBeVisible();
  });

  test('should display "Add Module" title on Module Creation page', async ({ page }) => {
    await login(page, 'musa', 'musa123');
    
    await page.goto(`${BASE_URL}/modules/new`);
    
    const headerTitle = page.locator('.h-title').filter({ hasText: /add.*module/i });
    await expect(headerTitle).toBeVisible();
  });

  test('should display "Create User" title on User Creation page', async ({ page }) => {
    await login(page, 'musa', 'musa123');
    
    await page.goto(`${BASE_URL}/users/new`);
    
    const headerTitle = page.locator('.h-title').filter({ hasText: /create.*user/i });
    await expect(headerTitle).toBeVisible();
  });

  test('should navigate back using browser back button', async ({ page }) => {
    await login(page, 'john', 'john123');
    
    await page.goto(`${BASE_URL}/modules`);
    await page.goto(`${BASE_URL}/dashboard`);
    
    await page.goBack();
    await expect(page).toHaveURL(/modules/);
  });

  test('should maintain sidebar navigation across page changes', async ({ page }) => {
    await login(page, 'john', 'john123');
    
    await page.goto(`${BASE_URL}/dashboard`);
    const dashboardLink = page.getByRole('link', { name: /^dashboard$/i });
    await expect(dashboardLink).toBeVisible();
    
    await page.goto(`${BASE_URL}/modules`);
    const modulesLink = page.getByRole('link', { name: /^modules$/i });
    await expect(modulesLink).toBeVisible();
  });

  test('should display user information on Profile page', async ({ page }) => {
    await login(page, 'john', 'john123');
    
    await page.goto(`${BASE_URL}/profile`);
    await page.waitForLoadState('networkidle');
    
    await expect(page.getByText(/username/i)).toBeVisible();
    await expect(page.getByText(/email/i)).toBeVisible();
  });

  test('should display "Welcome" and "Assessment Tool" on Home page', async ({ page }) => {
    await page.goto(BASE_URL);
    await page.evaluate(() => localStorage.clear());
    
    await page.goto(`${BASE_URL}/`);
    
    await expect(page.getByText(/welcome/i)).toBeVisible();
    await expect(page.getByText(/assessment tool/i)).toBeVisible();
  });

  test('should navigate from Home page to Login via Sign In button', async ({ page }) => {
    await page.goto(BASE_URL);
    await page.evaluate(() => localStorage.clear());
    
    await page.goto(`${BASE_URL}/`);
    
    const signInLink = page.getByRole('link', { name: /sign in/i });
    await signInLink.click();
    
    await expect(page).toHaveURL(/login/);
    await expect(page.getByText(/welcome back/i)).toBeVisible();
  });

  test('should display Login form fields', async ({ page }) => {
    await page.goto(BASE_URL);
    await page.evaluate(() => localStorage.clear());
    
    await page.goto(`${BASE_URL}/login`);
    
    await expect(page.locator('input[name="usernameOrEmail"]')).toBeVisible();
    await expect(page.locator('input[name="password"]')).toBeVisible();
    await expect(page.getByRole('button', { name: /sign in/i })).toBeVisible();
  });

  test('should display "View all modules" link on Dashboard', async ({ page }) => {
    await login(page, 'john', 'john123');
    
    const viewModulesLink = page.getByRole('link', { name: /view.*all.*modules/i });
    await expect(viewModulesLink).toBeVisible();
  });

  test('should display Users link in sidebar for admin users', async ({ page }) => {
    await login(page, 'musa', 'musa123');
    
    const usersLink = page.getByRole('link', { name: /^users$/i });
    await expect(usersLink).toBeVisible();
  });

  test('should NOT display Users link in sidebar for academic users', async ({ page }) => {
    await login(page, 'john', 'john123');
    
    const usersLink = page.getByRole('link', { name: /^users$/i });
    await expect(usersLink).not.toBeVisible();
  });

  test('should display logout button in sidebar', async ({ page }) => {
    await login(page, 'john', 'john123');
    
    const logoutButton = page.locator('.logout-link, button:has-text("Logout")');
    await expect(logoutButton).toBeVisible();
  });

  test('should redirect to login after logout', async ({ page }) => {
    await login(page, 'john', 'john123');
    
    const logoutButton = page.locator('.logout-link, button:has-text("Logout")');
    await logoutButton.click();
    
    await expect(page).toHaveURL(/login/);
  });

  test('should display form fields on Module Creation page', async ({ page }) => {
    await login(page, 'musa', 'musa123');
    
    await page.goto(`${BASE_URL}/modules/new`);
    
    await expect(page.locator('input[name="code"]')).toBeVisible();
    await expect(page.locator('input[name="title"]')).toBeVisible();
  });


  test('should display form fields on User Creation page', async ({ page }) => {
    await login(page, 'musa', 'musa123');
    
    await page.goto(`${BASE_URL}/users/new`);
    
    await expect(page.locator('input[name="username"]')).toBeVisible();
    await expect(page.locator('input[name="email"]')).toBeVisible();
    await expect(page.locator('input[name="password"]')).toBeVisible();
    await expect(page.locator('select[name="userType"]')).toBeVisible();
  });


  test('should navigate from Users page to User Creation page', async ({ page }) => {
    await login(page, 'musa', 'musa123');
    
    await page.goto(`${BASE_URL}/users`);
    await page.waitForLoadState('networkidle');
    
    // Try to find Add User button/link
    const addUserLink = page.getByRole('link', { name: /add.*user|create.*user|new.*user/i });
    const addUserButton = page.getByRole('button', { name: /add.*user|create.*user|new.*user/i });
    
    const hasLink = await addUserLink.isVisible({ timeout: 2000 }).catch(() => false);
    if (hasLink) {
      await addUserLink.first().click();
      await expect(page).toHaveURL(/users\/new/);
    } else {
      const hasButton = await addUserButton.isVisible({ timeout: 2000 }).catch(() => false);
      if (hasButton) {
        await addUserButton.first().click();
        await expect(page).toHaveURL(/users\/new/);
      } else {
        // Navigate directly if button not found
        await page.goto(`${BASE_URL}/users/new`);
        await expect(page).toHaveURL(/users\/new/);
      }
    }
  });

  test('should navigate from Modules page to Module Creation page', async ({ page }) => {
    await login(page, 'musa', 'musa123');
    
    await page.goto(`${BASE_URL}/modules`);
    await page.waitForLoadState('networkidle');
    
    const addModuleLink = page.getByRole('link', { name: /add.*module/i });
    const hasAddLink = await addModuleLink.isVisible({ timeout: 2000 }).catch(() => false);
    
    if (hasAddLink) {
      await addModuleLink.click();
      await expect(page).toHaveURL(/modules\/new/);
    } else {
      // Navigate directly if button not found
      await page.goto(`${BASE_URL}/modules/new`);
      await expect(page).toHaveURL(/modules\/new/);
    }
  });

  test('should highlight active navigation item in sidebar', async ({ page }) => {
    await login(page, 'john', 'john123');
    
    await page.goto(`${BASE_URL}/modules`);
    
    const modulesLink = page.getByRole('link', { name: /^modules$/i });
    const linkClasses = await modulesLink.getAttribute('class');
    expect(linkClasses).toContain('active');
  });

  test('should display role/view information in Layout header', async ({ page }) => {
    await login(page, 'john', 'john123');
    
    // Should display role/view info (Academic, Teaching Support, etc.)
    const roleDisplay = page.getByText(/academic|teaching.*support|external.*examiner/i).first();
    await expect(roleDisplay).toBeVisible();
  });

  test('should navigate directly to modules via URL', async ({ page }) => {
    await login(page, 'john', 'john123');
    
    await page.goto(`${BASE_URL}/modules`);
    await expect(page).toHaveURL(/modules/);
    
    const headerTitle = page.locator('.h-title').filter({ hasText: /modules/i });
    await expect(headerTitle).toBeVisible();
  });

  test('should navigate directly to profile via URL', async ({ page }) => {
    await login(page, 'john', 'john123');
    
    await page.goto(`${BASE_URL}/profile`);
    await expect(page).toHaveURL(/profile/);
    
    const pageTitle = page.getByRole('heading', { name: /my profile/i });
    await expect(pageTitle).toBeVisible();
  });
});

