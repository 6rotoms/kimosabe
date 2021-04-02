import React from 'react';
import { render, fireEvent, screen, waitFor } from '../test-utils';
import RegisterPage from '../../pages/register';
import { register } from '../../services/authService';
import { REGISTER_ERROR_MESSAGES } from '../../constants';

jest.mock('../../services/authService', () => ({
  register: jest.fn(),
}));

describe('pages/register.js', () => {
  it('should render', () => {
    // Act
    render(<RegisterPage />);
    // Assert
    expect(screen.getByTestId('rp-username').value).toBe('');
    expect(screen.getByTestId('rp-password').value).toBe('');
    expect(screen.getByTestId('rp-cpassword').value).toBe('');
  });

  describe('when username is changed', () => {
    test('then state should change', async () => {
      // Act
      const { findByTestId } = render(<RegisterPage />);
      const field = await findByTestId('rp-username');
      fireEvent.change(field, { target: { value: 'newusername' } });

      // Assert
      expect(screen.getByTestId('rp-username').value).toBe('newusername');
    });
  });

  describe('when password is changed', () => {
    test('then state should change', async () => {
      // Act
      const { findByTestId } = render(<RegisterPage />);
      const field = await findByTestId('rp-password');
      fireEvent.change(field, { target: { value: 'newpassword' } });

      // Assert
      expect(screen.getByTestId('rp-password').value).toBe('newpassword');
    });
  });

  describe('when confirm password is changed', () => {
    test('then state should change', async () => {
      // Act
      const { findByTestId } = render(<RegisterPage />);
      const field = await findByTestId('rp-cpassword');
      fireEvent.change(field, { target: { value: 'newpassword' } });

      // Assert
      expect(screen.getByTestId('rp-cpassword').value).toBe('newpassword');
    });
  });

  describe('when register button is clicked', () => {
    describe('all fields valid and register succeeds', () => {
      test('no errors should be displayed', async () => {
        // Arrange
        const response = {
          status: 200,
        };
        register.mockReturnValueOnce(response);

        // Act
        render(<RegisterPage />);
        fireEvent.change(screen.getByTestId('rp-username'), { target: { value: 'user' } });
        fireEvent.change(screen.getByTestId('rp-password'), { target: { value: 'pass' } });
        fireEvent.change(screen.getByTestId('rp-cpassword'), { target: { value: 'pass' } });
        fireEvent.click(screen.getByTestId('rp-register-button'));
        await waitFor(() => {});

        // Assert
        expect(screen.getByTestId('rp-username-error').textContent).toBe('');
        expect(screen.getByTestId('rp-password-error').textContent).toBe('');
        expect(screen.getByTestId('rp-cpassword-error').textContent).toBe('');
      });
    });

    describe('all fields valid but username is taken', () => {
      test('error should be returned and displayed', async () => {
        // Arrange
        const response = {
          status: 409,
          error: REGISTER_ERROR_MESSAGES[409],
        };
        register.mockReturnValueOnce(response);

        // Act
        render(<RegisterPage />);
        fireEvent.change(screen.getByTestId('rp-username'), { target: { value: 'user' } });
        fireEvent.change(screen.getByTestId('rp-password'), { target: { value: 'pass' } });
        fireEvent.change(screen.getByTestId('rp-cpassword'), { target: { value: 'pass' } });
        fireEvent.click(screen.getByTestId('rp-register-button'));
        await waitFor(() => {});

        // Assert
        expect(screen.getByTestId('rp-username-error').textContent).toBe(REGISTER_ERROR_MESSAGES[409]);
      });
    });

    describe('username/password fields less than 3 characters long', () => {
      test('error should be displayed', () => {
        // Act
        render(<RegisterPage />);
        fireEvent.click(screen.getByTestId('rp-register-button'));

        // Assert
        expect(screen.getByTestId('rp-username-error').textContent).toBe(REGISTER_ERROR_MESSAGES.USERNAME_TOO_SHORT);
        expect(screen.getByTestId('rp-password-error').textContent).toBe(REGISTER_ERROR_MESSAGES.PASSWORD_TOO_SHORT);
      });
    });

    describe('password and confirm password fields do not match', () => {
      test('error should be displayed', async () => {
        // Act
        render(<RegisterPage />);
        const field = screen.getByTestId('rp-password');
        fireEvent.change(field, { target: { value: 'pass' } });
        fireEvent.click(screen.getByTestId('rp-register-button'));

        // Assert
        expect(screen.getByTestId('rp-cpassword-error').textContent).toBe(
          REGISTER_ERROR_MESSAGES.CONFIRM_PASSWORD_FAILED,
        );
      });
    });
  });
});
