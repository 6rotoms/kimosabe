import { LOGIN_FAILED, LOGIN_SUCCESS } from '../../constants';

export const initialState = {
  loginError: '',
};

function errorReducer(state = initialState, action) {
  switch (action.type) {
    case LOGIN_FAILED:
      return {
        ...state,
        loginError: action.errorMessage,
      };
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
