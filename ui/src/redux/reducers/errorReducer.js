import { REGISTER_FAILED, REGISTER_SUCCESS, LOGIN_FAILED, LOGIN_SUCCESS } from '../../constants';

export const initialState = {
  registerError: '',
  loginError: '',
};

function errorReducer(state = initialState, action) {
  switch (action.type) {
    case REGISTER_FAILED: {
      return {
        ...state,
        registerError: action.errorMessage,
      };
    }
    case REGISTER_SUCCESS:
      return {
        ...state,
        registerError: '',
      };
    case LOGIN_FAILED: {
      return {
        ...state,
        loginError: action.errorMessage,
      };
    }
    case LOGIN_SUCCESS:
      return {
        ...state,
        loginError: '',
      };
    default:
      return state;
  }
}

export default errorReducer;
