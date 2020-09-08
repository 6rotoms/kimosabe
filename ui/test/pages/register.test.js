import React from 'react';
import { useStaticQuery } from 'gatsby';

import graphql from '../__mocks__/graphql';
import { render, fireEvent, screen, waitFor } from '../test-utils';
import RegisterPage from '../../src/pages/register';
import { register } from '../../src/services/authService';
import { makeStore } from '../../src/redux/store';
import { REGISTER_ERROR_MESSAGES } from '../../src/constants/auth';

jest.mock('../../src/services/authService', () => ({
  register: jest.fn(),
}));

beforeEach(() => {
    useStaticQuery
        .mockImplementationOnce(graphql.layoutQuery)
        .mockImplementationOnce(graphql.SEOQuery);
});

describe('pages/register.js', () => {
    it('should render', () => {
      // Arrange
      const store = makeStore();
      // Act
      render(<RegisterPage />, {store});

      // Assert
      expect(screen.getByTestId('rp-username').value).toBe('');
      expect(screen.getByTestId('rp-password').value).toBe('');
      expect(screen.getByTestId('rp-cpassword').value).toBe('');
    });

    describe('when username is changed', () => {
        test('then state should change', async () => {
            // Arrange
            const store = makeStore();

            // Act
            const { findByTestId } = render(<RegisterPage />, {store});
            const field = await findByTestId('rp-username');
            fireEvent.change(field, { target: { value: 'newusername' } });

            // Assert
            expect(screen.getByTestId('rp-username').value).toBe('newusername');
        });
    });

    describe('when password is changed', () => {
        test('then state should change', async () => {
            // Arrange
            const store = makeStore();

            // Act
            const { findByTestId } = render(<RegisterPage />, {store});
            const field = await findByTestId('rp-password');
            fireEvent.change(field, { target: { value: 'newpassword' } });

            // Assert
            expect(screen.getByTestId('rp-password').value).toBe('newpassword');
        });
    });

    describe('when confirm password is changed', () => {
        test('then state should change', async () => {
            // Arrange
            const store = makeStore();

            // Act
            const { findByTestId } = render(<RegisterPage />, {store});
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
                const store = makeStore();
                const response = {
                    status: 200,
                };
                register.mockReturnValueOnce(response);

                // Act
                render(<RegisterPage/>, {store});
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
                const store = makeStore();
                const response = {
                    status: 409,
                    error: REGISTER_ERROR_MESSAGES[409],
                  };
                register.mockReturnValueOnce(response);

                // Act
                render(<RegisterPage/>, {store});
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
                // Arrange
                const store = makeStore();

                // Act
                render(<RegisterPage/>, {store});
                fireEvent.click(screen.getByTestId('rp-register-button'));

                // Assert
                expect(screen.getByTestId('rp-username-error').textContent).toBe(REGISTER_ERROR_MESSAGES.USERNAME_TOO_SHORT);
                expect(screen.getByTestId('rp-password-error').textContent).toBe(REGISTER_ERROR_MESSAGES.PASSWORD_TOO_SHORT);
            });
        });

        describe('password and confirm password fields do not match', () => {
            test('error should be displayed', async () => {
                // Arrange
                const store = makeStore();

                // Act
                render(<RegisterPage/>, {store});
                const field = screen.getByTestId('rp-password');
                fireEvent.change(field, { target: { value: 'pass' } });
                fireEvent.click(screen.getByTestId('rp-register-button'));

                // Assert
                expect(screen.getByTestId('rp-cpassword-error').textContent).toBe(REGISTER_ERROR_MESSAGES.CONFIRM_PASSWORD_FAILED);
            });
        });
    });
});