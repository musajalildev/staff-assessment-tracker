// e2e/addUser.spec.js
import { test, expect } from '@playwright/test';

test('musa can see existing test user in user list', async ({ page }) => {
  const BASE_URL = 'http://localhost:5173';

  // Musa login credentials
  const loginUsername = 'musa';
  const loginPassword = 'musa123';

  // Step 1: Go to login page
  await page.goto(`${BASE_URL}/login`);

  // Step 2: Fill in login form
  await page.fill('input[name="usernameOrEmail"]', loginUsername);
  await page.fill('input[name="password"]', loginPassword);

  // Step 3: Submit login and wait for dashboard
  await Promise.all([
    page.waitForURL('**/dashboard'),
    page.click('button[type="submit"]')
  ]);

  // Step 4: Navigate to Users page
  await Promise.all([
    page.waitForURL('**/users'),
    page.click('text=Users')
  ]);

  // Step 5: Assert the Users page loaded
  await expect(page).toHaveURL(/.*\/users/);

  // Step 6: Check that testuser1 exists in the table
  // FIX: Playwright complained about strict mode due to duplicate matches
  await expect(
    page.getByRole('cell', { name: 'testuser1', exact: true })
  ).toBeVisible();
});
