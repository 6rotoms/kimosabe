import * as types from '../../../src/constants/actions';
import reducer, { initialState } from '../../../src/redux/reducers/errorReducer';

describe('redux/reducers/errorReducer.js', () => {
  describe('when undefined action is called', () => {
    test('then it should return the initial state', () => {
      // Act
      const result = reducer(undefined, {});
      // Assert
      expect(result).toEqual(initialState);
    });
  });

  describe('when REGISTER_FAILED action is called', () => {
    test('then error message should be set', () => {
      // Arrange
      const action = { type: types.REGISTER_FAILED, errorMessage: 'custom error message' };

      // Act
      const result = reducer({ ...initialState }, action);

      // Assert
      expect(result).toEqual({ ...initialState, registerError: 'custom error message' });
    });
  });

  describe('when REGISTER_SUCCESS action is called', () => {
    test('then there should be no error message', () => {
      // Arrange
      const action = { type: types.REGISTER_SUCCESS };

      // Act
      const result = reducer({ ...initialState, registerError: 'had error before' }, action);

      // Assert
      expect(result).toEqual({ ...initialState, registerError: '' });
    });
  });

  describe('when LOGIN_FAILED action is called', () => {
    test('then error message should be set', () => {
      // Arrange
      const action = { type: types.LOGIN_FAILED, errorMessage: 'custom error message' };

      // Act
      const result = reducer({ ...initialState }, action);

      // Assert
      expect(result).toEqual({ ...initialState, loginError: 'custom error message' });
    });
  });

  describe('when LOGIN_SUCCESS action is called', () => {
    test('then isLoading should be set to false', () => {
      // Arrange
      const action = { type: types.LOGIN_SUCCESS };

      // Act
      const result = reducer({ ...initialState, loginError: 'had error before' }, action);

      // Assert
      expect(result).toEqual({ ...initialState, loginError: '' });
    });
  });
});
