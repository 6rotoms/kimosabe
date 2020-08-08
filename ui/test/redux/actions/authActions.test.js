import configureMockStore from 'redux-mock-store';
import thunk from 'redux-thunk';

import * as actions from '../../../src/redux/actions/authActions';
import * as types from '../../../src/constants/actions';
import { initialState as initialAuthState } from '../../../src/redux/reducers/authReducer';
import { initialState as initialErrorState } from '../../../src/redux/reducers/errorReducer';
import { navigate } from '../../__mocks__/gatsby';
import { LOGIN_ERROR_MESSAGES, LOGOUT_ERROR_MESSAGES } from '../../../src/constants';
import { login, logout } from '../../../src/services/authService';

jest.mock('../../../src/services/authService', () => ({
  login: jest.fn(),
  logout: jest.fn(),
}));

const middlewares = [thunk];
const mockStore = configureMockStore(middlewares);
let store;

describe('redux/actions/authActions.js', () => {
  beforeEach(() => {
    store = mockStore({ auth: initialAuthState, errors: initialErrorState });
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  describe('when login requested', () => {
    describe('and request has no error', () => {
      describe('and request returns no error', () => {
        let expectedActions;
        beforeEach(async () => {
          // Arrange
          const response = {
            status: 200,
          };
          login.mockReturnValueOnce(response);
          expectedActions = [
            { type: types.LOGIN_REQUEST },
            { type: types.LOGIN_SUCCESS },
          ];

          // Act
          await store.dispatch(
            actions.loginUser({ username: '', password: '' }),
          );
        });

        test('then LOGIN_SUCCESS should be dispatched', async () => {
          // Assert
          expect(store.getActions()).toEqual(expectedActions);
        });

        test('then redirect to home should occur', async () => {
          // Assert
          expect(navigate).toHaveBeenCalledWith('/');
        });
      });

      describe('and request returns 403', () => {
        let expectedActions;
        beforeEach(async () => {
          // Arrange
          const response = {
            status: 403,
            error: LOGIN_ERROR_MESSAGES[403],
          };
          login.mockReturnValueOnce(response);
          expectedActions = [
            { type: types.LOGIN_REQUEST },
            {
              type: types.LOGIN_FAILED,
              errorMessage: LOGIN_ERROR_MESSAGES[403],
            },
          ];

          // Act
          await store.dispatch(
            actions.loginUser({ username: '', password: '' }),
          );
        });

        test('then LOGIN_FAILED should be dispatched', () => {
          expect(store.getActions()).toEqual(expectedActions);
        });
      });

      describe('and request returns 500', () => {
        let expectedActions;
        beforeEach(async () => {
          // Arrange
          const response = {
            status: 500,
            error: LOGIN_ERROR_MESSAGES[500],
          };
          login.mockReturnValueOnce(response);
          expectedActions = [
            { type: types.LOGIN_REQUEST },
            {
              type: types.LOGIN_FAILED,
              errorMessage: LOGIN_ERROR_MESSAGES[500],
            },
          ];

          // Act
          await store.dispatch(
            actions.loginUser({ username: '', password: '' }),
          );
        });

        test('then LOGIN_FAILED should be dispatched', () => {
          expect(store.getActions()).toEqual(expectedActions);
        });
      });

      describe('and request returns unspecified', () => {
        let expectedActions;
        beforeEach(async () => {
          // Arrange
          const response = {
            status: 501,
            error: LOGIN_ERROR_MESSAGES.UNDEFINED,
          };
          login.mockReturnValueOnce(response);
          expectedActions = [
            { type: types.LOGIN_REQUEST },
            {
              type: types.LOGIN_FAILED,
              errorMessage: LOGIN_ERROR_MESSAGES.UNDEFINED,
            },
          ];

          // Act
          await store.dispatch(
            actions.loginUser({ username: '', password: '' }),
          );
        });

        test('then LOGIN_FAILED should be dispatched', () => {
          expect(store.getActions()).toEqual(expectedActions);
        });
      });
    });
  });

  describe('when logout requested', () => {
    describe('and request has no error', () => {
      describe('and logout returns no error', () => {
        let expectedActions;
        beforeEach(async () => {
          // Arrange
          const response = {
            status: 200,
          };
          logout.mockReturnValueOnce(response);
          expectedActions = [
            { type: types.LOGOUT_REQUEST },
            { type: types.LOGOUT_SUCCESS },
          ];

          // Act
          await store.dispatch(actions.logoutUser());
        });

        test('then LOGOUT_SUCCESS should be dispatched', () => {
          expect(store.getActions()).toEqual(expectedActions);
        });
      });
      describe('and logout returns error', () => {
        let expectedActions;
        beforeEach(async () => {
          // Arrange
          const response = {
            status: 500,
            error: LOGOUT_ERROR_MESSAGES[500],
          };
          logout.mockReturnValueOnce(response);
          expectedActions = [
            { type: types.LOGOUT_REQUEST },
            { type: types.LOGOUT_FAILED },
          ];

          // Act
          await store.dispatch(actions.logoutUser());
        });

        test('then LOGOUT_FAILED should be dispatched', () => {
          expect(store.getActions()).toEqual(expectedActions);
        });
      });
    });
  });
});