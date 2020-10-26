import * as types from '../../../constants/actions';
import reducer, { initialState } from '../../../redux/reducers/authReducer';

describe('redux/reducers/authReducer.js', () => {
  describe('when undefined action is called', () => {
    test('then it should return the initial state', () => {
      // Act
      const result = reducer(undefined, {});
      // Assert
      expect(result).toEqual(initialState);
    });
  });

  describe('when REGISTER_REQUEST action is called', () => {
    test('then isLoading should be set to true', () => {
      // Arrange
      const action = { type: types.REGISTER_REQUEST };

      // Act
      const result = reducer(undefined, action);

      // Assert
      expect(result).toEqual({ ...initialState, isLoading: true });
    });
  });

  describe('when REGISTER_FAILED action is called', () => {
    test('then isLoading should be set to false', () => {
      // Arrange
      const action = { type: types.REGISTER_FAILED };

      // Act
      const result = reducer({ ...initialState, isLoading: true }, action);

      // Assert
      expect(result).toEqual({ ...initialState });
    });
  });

  describe('when REGISTER_SUCCESS action is called', () => {
    test('then isLoading should be set to false', () => {
      // Arrange
      const action = { type: types.REGISTER_SUCCESS };

      // Act
      const result = reducer({ ...initialState, isLoading: true }, action);

      // Assert
      expect(result).toEqual({ ...initialState });
    });
  });

  describe('when LOGIN_REQUEST action is called', () => {
    test('then isLoading should be set to true', () => {
      // Arrange
      const action = { type: types.LOGIN_REQUEST };

      // Act
      const result = reducer(undefined, action);

      // Assert
      expect(result).toEqual({ ...initialState, isLoading: true });
    });
  });

  describe('when LOGIN_FAILED action is called', () => {
    test('then isLoading should be set to false', () => {
      // Arrange
      const action = { type: types.LOGIN_FAILED };

      // Act
      const result = reducer({ ...initialState, isLoading: true }, action);

      // Assert
      expect(result).toEqual({ ...initialState });
    });
  });

  describe('when LOGIN_SUCCESS action is called', () => {
    test('then isLoading should be set to true', () => {
      // Arrange
      const action = { type: types.LOGIN_SUCCESS };

      // Act
      const result = reducer({ ...initialState, isLoading: true }, action);

      // Assert
      expect(result).toEqual({ ...initialState, loggedIn: true });
    });
  });

  describe('when LOGOUT_REQUEST action is called', () => {
    test('then isLoading should be set to true', () => {
      // Arrange
      const action = { type: types.LOGOUT_REQUEST };

      // Act
      const result = reducer(undefined, action);

      // Assert
      expect(result).toEqual({ ...initialState, isLoading: true });
    });
  });

  describe('when LOGOUT_FAILED action is called', () => {
    test('then isLoading should be set to false', () => {
      // Arrange
      const action = { type: types.LOGOUT_FAILED };

      // Act
      const result = reducer({ ...initialState, isLoading: true }, action);

      // Assert
      expect(result).toEqual({ ...initialState });
    });
  });

  describe('when LOGOUT_SUCCESS action is called', () => {
    test('then isLoading should be set to true', () => {
      // Arrange
      const action = { type: types.LOGOUT_SUCCESS };

      // Act
      const result = reducer({ ...initialState, isLoading: true, loggedIn: true }, action);

      // Assert
      expect(result).toEqual({ ...initialState, loggedIn: false });
    });
  });
});
