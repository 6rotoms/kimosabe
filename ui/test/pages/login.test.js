import React from 'react';
import { useStaticQuery } from 'gatsby';

import graphql from '../__mocks__/graphql';
import { render, fireEvent, screen, makeTestStore, waitFor } from '../test-utils';
import LoginPage from '../../src/pages/login';
import { login } from '../../src/services/authService';
import { makeStore } from '../../src/redux/store';
import { LOGIN_ERROR_MESSAGES } from '../../src/constants/auth';

jest.mock('../../src/services/authService', () => ({
  login: jest.fn(),
}));

beforeEach(() => {
  useStaticQuery
    .mockImplementationOnce(graphql.layoutQuery)
    .mockImplementationOnce(graphql.SEOQuery);
});


describe('pages/login.js', () => {
  it('should render', () => {
    // Arrange
    const store = makeStore();
    // Act
    render(<LoginPage />, {store});

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
        render(<LoginPage />, {store});

        // Assert
        fireEvent.click(screen.getByTestId('login-submit-button'));
        expect(store.dispatch).toHaveBeenCalledWith(expect.any(Function));
    });

    describe('and login succeeds', () => {
      beforeEach(() => {

      });

      test('then error should be set', async () => {
        // Arrange
        const store = makeStore();
        const response = {
          status: 200,
        };
        login.mockReturnValueOnce(response);

        // Act
        render(<LoginPage />, {store});
        fireEvent.click(screen.getByTestId('login-submit-button'));
        await waitFor(() => {});

        // Assert
        expect(screen.getByTestId('error-span').textContent).toBe('');
      });
    });

    describe('and login fails', () => {

      test('then error should be set', async () => {
        // Arrange
        const store = makeStore();
        const response = {
          status: 403,
          error: LOGIN_ERROR_MESSAGES[403],
        };
        login.mockReturnValueOnce(response);

        // Act
        const { getByText } = render(<LoginPage />, {store});
        fireEvent.click(screen.getByTestId('login-submit-button'));
        await waitFor(() => {});

        // Assert
        const element = await getByText(LOGIN_ERROR_MESSAGES[403]);
        expect(element).toBeDefined();
      });
    });
  });

  describe('when username is changed', () => {
    test('then state should change', async () => {
      // Arrange
      const store = makeStore();

      // Act
      const { findByTestId } = render(<LoginPage />, {store});
      const field = await findByTestId('username-field');
      fireEvent.change(field, { target: { value: 'newusername' } });

      // Assert
      expect(screen.getByTestId('username-field').value).toBe('newusername');
    });
  });

  describe('when password is changed', () => {
    test('then state should change', async () => {
      // Arrange
      const store = makeStore();

      // Act
      const { findByTestId } = render(<LoginPage />, {store});
      const field = await findByTestId('password-field');
      fireEvent.change(field, { target: { value: 'newpassword' } });

      // Assert
      expect(screen.getByTestId('password-field').value).toBe('newpassword');
    });
  });
});
