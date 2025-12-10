// e2e/login.spec.js
import { test, expect } from '@playwright/test';

test('user can log in successfully', async ({ page }) => {
  await page.goto('http://localhost:5173/login');

  await page.fill('input[name="usernameOrEmail"]', 'john');
  await page.fill('input[name="password"]', 'john123');

  await page.click('button[type="submit"]');

  await expect(page).toHaveURL(/dashboard/); // or adjust if redirected elsewhere
  await expect(page.getByText(/dashboard/i)).toBeVisible();
});
