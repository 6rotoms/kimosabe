import configureMockStore from 'redux-mock-store';
import thunk from 'redux-thunk';
import fetchMock from 'fetch-mock';

import * as actions from '../../../src/redux/actions/authActions';
import * as types from '../../../src/constants/actions';
import { initialState as initialAuthState } from '../../../src/redux/reducers/authReducer';
import { initialState as initialErrorState } from '../../../src/redux/reducers/errorReducer';
import { navigate } from '../../__mocks__/gatsby';
import { LOGIN_ERROR_MESSAGES } from '../../../src/constants';

const middlewares = [thunk];
const mockStore = configureMockStore(middlewares);
let store;

describe('redux/actions/authActions.js', () => {
  beforeEach(() => {
    store = mockStore({ auth: initialAuthState, errors: initialErrorState });
  });

  afterEach(() => {
    fetchMock.restore();
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
          fetchMock.post(`${process.env.GATSBY_API_URL}/auth/login`, response);
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
          };
          fetchMock.post(`${process.env.GATSBY_API_URL}/auth/login`, response);
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
          };
          fetchMock.post(`${process.env.GATSBY_API_URL}/auth/login`, response);
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
          };
          fetchMock.post(`${process.env.GATSBY_API_URL}/auth/login`, response);
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
    describe('and request has error', () => {
      let expectedActions;
      beforeEach(async () => {
        // Arrange
        fetchMock.post(`${process.env.GATSBY_API_URL}/auth/login`, () => {
          throw new Error('error thrown');
        });
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

  describe('when logout requested', () => {
    describe('and request has no error', () => {
      describe('and logout returns no error', () => {
        let expectedActions;
        beforeEach(async () => {
          // Arrange
          const response = {
            status: 200,
          };
          fetchMock.post(`${process.env.GATSBY_API_URL}/auth/logout`, response);
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
            status: 400,
          };
          fetchMock.post(`${process.env.GATSBY_API_URL}/auth/logout`, response);
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
    describe('and request has error', () => {
      let expectedActions;
      beforeEach(async () => {
        // Arrange
        fetchMock.post(`${process.env.GATSBY_API_URL}/auth/logout`, () => {
          throw new Error('error thrown');
        });
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
