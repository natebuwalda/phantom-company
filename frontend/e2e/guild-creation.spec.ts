import { expect, test } from '@playwright/test'

test('a created Training Guild survives a browser reload', async ({ page }) => {
  await page.goto('/')

  await page.getByRole('radio', { name: /Training Guild/i }).click()
  await page.getByRole('button', { name: 'Sign the charter' }).click()

  await expect(page.getByRole('heading', { name: 'Training Guild' })).toBeVisible()
  await expect(page.getByText('10,000 gold')).toBeVisible()
  await expect(page.getByRole('heading', { name: 'Phantom Squire' })).toBeVisible()
  await expect(page.getByText('Regimen')).toBeVisible()
  await expect(page.getByText('Veteran')).toBeVisible()
  await expect(page.getByText('Statue')).toBeVisible()

  await page.reload()

  await expect(page.getByRole('heading', { name: 'Training Guild' })).toBeVisible()
  await expect(page.getByText('10,000 gold')).toBeVisible()
})
