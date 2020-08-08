import { LOGIN_REQUEST, LOGIN_FAILED, LOGIN_SUCCESS } from '../../constants';

export const initialState = {
  isLoading: false,
};

function authReducer(state = initialState, action) {
  switch (action.type) {
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
      };
    default:
      return state;
  }
}

export default authReducer;
