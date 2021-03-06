import React from 'react';
import { render, fireEvent, screen, waitFor } from '../test-utils';
import LoginPage from '../../pages/login';
import { login } from '../../services/authService';
import { LOGIN_ERROR_MESSAGES } from '../../constants';

jest.mock('../../services/authService', () => ({
  login: jest.fn(),
}));

describe('pages/login.js', () => {
  it('should render', () => {
    // Arrange
    // Act
    render(<LoginPage />);

    // Assert
    expect(screen.getByTestId('username-field').value).toBe('');
    expect(screen.getByTestId('password-field').value).toBe('');
  });

  describe('when login button clicked', () => {
    test('then login should be called', async () => {
      // Arrange
      const response = {
        status: 200,
      };
      login.mockReturnValueOnce(response);
      // Act
      render(<LoginPage />);
      fireEvent.click(screen.getByTestId('login-submit-button'));

      // Assert
      await waitFor(() => {
        expect(login).toHaveBeenCalledWith(expect.any(Object));
      });
    });

    describe('and login succeeds', () => {
      beforeEach(() => {});

      test('then error should not exist', async () => {
        // Arrange
        const response = {
          status: 200,
        };
        login.mockReturnValueOnce(response);

        // Act
        render(<LoginPage />);
        fireEvent.click(screen.getByTestId('login-submit-button'));
        await waitFor(() => {});

        // Assert
        expect(screen.queryByTestId('error-span')).toBeNull();
      });
    });

    describe('and login fails', () => {
      test('then error should be set', async () => {
        // Arrange
        const response = {
          status: 401,
          error: LOGIN_ERROR_MESSAGES[401],
        };
        login.mockReturnValueOnce(response);

        // Act
        const { getByText } = render(<LoginPage />);
        fireEvent.click(screen.getByTestId('login-submit-button'));
        await waitFor(() => {});

        // Assert
        const element = getByText(LOGIN_ERROR_MESSAGES[401]);
        expect(element).toBeDefined();
      });
    });
  });

  describe('when username is changed', () => {
    test('then state should change', async () => {
      // Act
      const { findByTestId } = render(<LoginPage />);
      const field = await findByTestId('username-field');
      fireEvent.change(field, { target: { value: 'newusername' } });

      // Assert
      expect(screen.getByTestId('username-field').value).toBe('newusername');
    });
  });

  describe('when password is changed', () => {
    test('then state should change', async () => {
      // Act
      const { findByTestId } = render(<LoginPage />);
      const field = await findByTestId('password-field');
      fireEvent.change(field, { target: { value: 'newpassword' } });

      // Assert
      expect(screen.getByTestId('password-field').value).toBe('newpassword');
    });
  });
});
