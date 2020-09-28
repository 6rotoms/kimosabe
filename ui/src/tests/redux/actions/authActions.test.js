import configureMockStore from 'redux-mock-store';
import thunk from 'redux-thunk';

import * as actions from '../../../redux/actions/authActions';
import * as types from '../../../constants/actions';
import { initialState as initialAuthState } from '../../../redux/reducers/authReducer';
import { initialState as initialErrorState } from '../../../redux/reducers/errorReducer';
import { REGISTER_ERROR_MESSAGES, LOGIN_ERROR_MESSAGES, LOGOUT_ERROR_MESSAGES } from '../../../constants';
import history from '../../../history';
import { register, login, logout } from '../../../services/authService';

jest.mock('../../../history', () => ({
  push: jest.fn(),
}));

jest.mock('../../../services/authService', () => ({
  register: jest.fn(),
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

  describe('when register requested', () => {
    describe('and request has no error', () => {
      describe('and request returns no error', () => {
        let expectedActions;
        beforeEach(async () => {
          // Arrange
          const response = {
            status: 200,
          };
          register.mockReturnValueOnce(response);
          expectedActions = [{ type: types.REGISTER_REQUEST }, { type: types.REGISTER_SUCCESS }];

          // Act
          await store.dispatch(actions.registerUser({ username: 'user', password: 'pass' }));
        });

        test('then REGISTER_SUCCESS should be dispatched', async () => {
          // Assert
          expect(store.getActions()).toEqual(expectedActions);
        });

        test('then redirect to home should occur', async () => {
          // Assert
          expect(history.push).toHaveBeenCalledWith('/');
        });
      });

      describe('and request returns 403', () => {
        let expectedActions;
        beforeEach(async () => {
          // Arrange
          const response = {
            status: 403,
            error: REGISTER_ERROR_MESSAGES[403],
          };
          register.mockReturnValueOnce(response);
          expectedActions = [
            { type: types.REGISTER_REQUEST },
            {
              type: types.REGISTER_FAILED,
              errorMessage: REGISTER_ERROR_MESSAGES[403],
            },
          ];

          // Act
          await store.dispatch(actions.registerUser({ username: '', password: '' }));
        });

        test('then REGISTER_FAILED should be dispatched', () => {
          expect(store.getActions()).toEqual(expectedActions);
        });
      });

      describe('and request returns 409', () => {
        let expectedActions;
        beforeEach(async () => {
          // Arrange
          const response = {
            status: 409,
            error: REGISTER_ERROR_MESSAGES[409],
          };
          register.mockReturnValueOnce(response);
          expectedActions = [
            { type: types.REGISTER_REQUEST },
            {
              type: types.REGISTER_FAILED,
              errorMessage: REGISTER_ERROR_MESSAGES[409],
            },
          ];

          // Act
          await store.dispatch(actions.registerUser({ username: '', password: '' }));
        });

        test('then REGISTER_FAILED should be dispatched', () => {
          expect(store.getActions()).toEqual(expectedActions);
        });
      });

      describe('and request returns 500', () => {
        let expectedActions;
        beforeEach(async () => {
          // Arrange
          const response = {
            status: 500,
            error: REGISTER_ERROR_MESSAGES[500],
          };
          register.mockReturnValueOnce(response);
          expectedActions = [
            { type: types.REGISTER_REQUEST },
            {
              type: types.REGISTER_FAILED,
              errorMessage: REGISTER_ERROR_MESSAGES[500],
            },
          ];

          // Act
          await store.dispatch(actions.registerUser({ username: '', password: '' }));
        });

        test('then REGISTER_FAILED should be dispatched', () => {
          expect(store.getActions()).toEqual(expectedActions);
        });
      });

      describe('and request returns unspecified', () => {
        let expectedActions;
        beforeEach(async () => {
          // Arrange
          const response = {
            status: 501,
            error: REGISTER_ERROR_MESSAGES.UNDEFINED,
          };
          register.mockReturnValueOnce(response);
          expectedActions = [
            { type: types.REGISTER_REQUEST },
            {
              type: types.REGISTER_FAILED,
              errorMessage: REGISTER_ERROR_MESSAGES.UNDEFINED,
            },
          ];

          // Act
          await store.dispatch(actions.registerUser({ username: '', password: '' }));
        });

        test('then REGISTER_FAILED should be dispatched', () => {
          expect(store.getActions()).toEqual(expectedActions);
        });
      });
    });
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
          expectedActions = [{ type: types.LOGIN_REQUEST }, { type: types.LOGIN_SUCCESS }];

          // Act
          await store.dispatch(actions.loginUser({ username: '', password: '' }));
        });

        test('then LOGIN_SUCCESS should be dispatched', async () => {
          // Assert
          expect(store.getActions()).toEqual(expectedActions);
        });

        test('then redirect to home should occur', async () => {
          // Assert
          expect(history.push).toHaveBeenCalledWith('/');
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
          await store.dispatch(actions.loginUser({ username: '', password: '' }));
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
          await store.dispatch(actions.loginUser({ username: '', password: '' }));
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
          await store.dispatch(actions.loginUser({ username: '', password: '' }));
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
          expectedActions = [{ type: types.LOGOUT_REQUEST }, { type: types.LOGOUT_SUCCESS }];

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
          expectedActions = [{ type: types.LOGOUT_REQUEST }, { type: types.LOGOUT_FAILED }];

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
