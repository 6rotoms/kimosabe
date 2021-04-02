import React from 'react';
import { render, fireEvent, screen, makeTestStore, waitFor } from '../test-utils';
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
    test('then loginUser action creater should be called', () => {
      // Arrange
      const store = makeTestStore();
      const response = {
        status: 200,
      };
      login.mockReturnValueOnce(response);
      // Act
      render(<LoginPage />, { store });

      // Assert
      fireEvent.click(screen.getByTestId('login-submit-button'));
      expect(store.dispatch).toHaveBeenCalledWith(expect.any(Function));
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
          status: 403,
          error: LOGIN_ERROR_MESSAGES[403],
        };
        login.mockReturnValueOnce(response);

        // Act
        const { getByText } = render(<LoginPage />);
        fireEvent.click(screen.getByTestId('login-submit-button'));
        await waitFor(() => {});

        // Assert
        const element = getByText(LOGIN_ERROR_MESSAGES[403]);
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
