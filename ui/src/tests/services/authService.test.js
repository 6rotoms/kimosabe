import fetchMock from 'fetch-mock';

import authService from '../../services/authService';
import { LOGIN_ERROR_MESSAGES, LOGOUT_ERROR_MESSAGES, UNDEFINED_ERROR_MESSAGE } from '../../constants';

describe('services/authService.js', () => {
  afterEach(() => {
    fetchMock.restore();
  });

  describe('when login is called', () => {
    describe('and request has no errors', () => {
      let serviceResponse;
      beforeEach(async () => {
        // Arrange
        const response = {
          status: 200,
        };
        fetchMock.post(`${process.env.REACT_APP_API_URL}/auth/login`, response);

        // Act
        serviceResponse = await authService.login({ username: '', password: '' });
      });

      test('then status 200 should be returned', () => {
        // Assert
        expect(serviceResponse).toEqual({ status: 200 });
      });
    });

    describe('and request returns 403 error', () => {
      let serviceResponse;
      beforeEach(async () => {
        // Arrange
        const response = {
          status: 403,
        };
        fetchMock.post(`${process.env.REACT_APP_API_URL}/auth/login`, response);

        // Act
        serviceResponse = await authService.login({ username: '', password: '' });
      });

      test('then status 200 should be returned', () => {
        // Assert
        expect(serviceResponse).toEqual({ status: 403, error: LOGIN_ERROR_MESSAGES[403] });
      });
    });

    describe('and request returns 500 error', () => {
      let serviceResponse;
      beforeEach(async () => {
        // Arrange
        const response = {
          status: 500,
        };
        fetchMock.post(`${process.env.REACT_APP_API_URL}/auth/login`, response);

        // Act
        serviceResponse = await authService.login({ username: '', password: '' });
      });

      test('then status 200 should be returned', () => {
        // Assert
        expect(serviceResponse).toEqual({ status: 500, error: LOGIN_ERROR_MESSAGES[500] });
      });
    });

    describe('and request returns 501 error', () => {
      let serviceResponse;
      beforeEach(async () => {
        // Arrange
        const response = {
          status: 501,
        };
        fetchMock.post(`${process.env.REACT_APP_API_URL}/auth/login`, response);

        // Act
        serviceResponse = await authService.login({ username: '', password: '' });
      });

      test('then status 501 should be returned', () => {
        // Assert
        expect(serviceResponse).toEqual({
          status: 501,
          error: UNDEFINED_ERROR_MESSAGE,
        });
      });
    });

    describe('and request throws error', () => {
      let serviceResponse;
      beforeEach(async () => {
        // Arrange
        fetchMock.post(`${process.env.REACT_APP_API_URL}/auth/login`, () => {
          throw new Error('error thrown');
        });

        // Act
        serviceResponse = await authService.login({ username: '', password: '' });
      });

      test('then status 500 should be returned', () => {
        // Assert
        expect(serviceResponse).toEqual({
          status: 500,
          error: UNDEFINED_ERROR_MESSAGE,
        });
      });
    });
  });

  describe('when logout is called', () => {
    describe('and request has no errors', () => {
      let serviceResponse;
      beforeEach(async () => {
        // Arrange
        const response = {
          status: 200,
        };
        fetchMock.post(`${process.env.REACT_APP_API_URL}/auth/logout`, response);

        // Act
        serviceResponse = await authService.logout();
      });

      test('then status 200 should be returned', () => {
        // Assert
        expect(serviceResponse).toEqual({ status: 200 });
      });
    });

    describe('and request returns 500 error', () => {
      let serviceResponse;
      beforeEach(async () => {
        // Arrange
        const response = {
          status: 500,
        };
        fetchMock.post(`${process.env.REACT_APP_API_URL}/auth/logout`, response);

        // Act
        serviceResponse = await authService.logout();
      });

      test('then status 500 should be returned', () => {
        // Assert
        expect(serviceResponse).toEqual({ status: 500, error: LOGOUT_ERROR_MESSAGES[500] });
      });
    });

    describe('and request returns 501 error', () => {
      let serviceResponse;
      beforeEach(async () => {
        // Arrange
        const response = {
          status: 501,
        };
        fetchMock.post(`${process.env.REACT_APP_API_URL}/auth/logout`, response);

        // Act
        serviceResponse = await authService.logout();
      });

      test('then status 501 should be returned', () => {
        // Assert
        expect(serviceResponse).toEqual({
          status: 501,
          error: UNDEFINED_ERROR_MESSAGE,
        });
      });
    });

    describe('and request throws error', () => {
      let serviceResponse;
      beforeEach(async () => {
        // Arrange
        fetchMock.post(`${process.env.REACT_APP_API_URL}/auth/logout`, () => {
          throw new Error('error thrown');
        });

        // Act
        serviceResponse = await authService.logout();
      });

      test('then status 500 should be returned', () => {
        // Assert
        expect(serviceResponse).toEqual({
          status: 500,
          error: UNDEFINED_ERROR_MESSAGE,
        });
      });
    });
  });
});
