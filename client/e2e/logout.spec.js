import { test, expect } from '@playwright/test';

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
