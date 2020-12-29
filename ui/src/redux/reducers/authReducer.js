import {
  REGISTER_REQUEST,
  REGISTER_FAILED,
  REGISTER_SUCCESS,
  LOGIN_REQUEST,
  LOGIN_FAILED,
  LOGIN_SUCCESS,
  LOGOUT_SUCCESS,
  LOGOUT_REQUEST,
  LOGOUT_FAILED,
} from '../../constants';

export const initialState = {
  isLoading: false,
  loggedIn: false,
};

function authReducer(state = initialState, action) {
  switch (action.type) {
    case REGISTER_REQUEST:
      return {
        ...state,
        isLoading: true,
      };
    case REGISTER_FAILED:
      return {
        ...state,
        isLoading: false,
      };
    case REGISTER_SUCCESS:
      return {
        ...state,
        isLoading: false,
      };
    case LOGIN_REQUEST:
      return {
        ...state,
        isLoading: true,
      };
    case LOGIN_FAILED:
      return {
        ...state,
        isLoading: false,
      };
    case LOGIN_SUCCESS:
      return {
        ...state,
        isLoading: false,
        loggedIn: true,
      };
    case LOGOUT_REQUEST:
      return {
        ...state,
        isLoading: true,
      };
    case LOGOUT_FAILED:
      return {
        ...state,
        isLoading: false,
      };
    case LOGOUT_SUCCESS:
      return {
        ...state,
        loggedIn: false,
        isLoading: false,
      };
    default:
      return state;
  }
}

export default authReducer;
