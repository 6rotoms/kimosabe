import 'cross-fetch/polyfill';
import { navigate } from 'gatsby';

import {
  LOGIN_REQUEST,
  LOGIN_SUCCESS,
  LOGIN_FAILED,
  LOGIN_ERROR_MESSAGES,
  LOGOUT_REQUEST,
  LOGOUT_SUCCESS,
  LOGOUT_FAILED,
} from '../../constants';

export const loginUser = ({ username, password }) => {
  return async (dispatch) => {
    dispatch({ type: LOGIN_REQUEST });

    try {
      const response = await fetch(`${process.env.GATSBY_API_URL}/auth/login`, {
        method: 'POST',
        headers: {
          username,
          password,
        },
      });
      if (response.status !== 200) {
        dispatch({
          type: LOGIN_FAILED,
          errorMessage:
            LOGIN_ERROR_MESSAGES[response.status] ||
            LOGIN_ERROR_MESSAGES.UNDEFINED,
        });
        return;
      }
      dispatch({
        type: LOGIN_SUCCESS,
      });
      navigate('/');
    } catch (error) {
      dispatch({
        type: LOGIN_FAILED,
        errorMessage: LOGIN_ERROR_MESSAGES.UNDEFINED,
      });
    }
  };
};

export const logoutUser = () => {
  const headers = new Headers();
  return async (dispatch) => {
    dispatch({
      type: LOGOUT_REQUEST,
    });
    try {
      const response = await fetch(
        `${process.env.GATSBY_API_URL}/auth/logout`,
        {
          method: 'POST',
          headers,
        },
      );
      if (response.status !== 200) {
        dispatch({
          type: LOGOUT_FAILED,
        });
        return;
      }
      dispatch({
        type: LOGOUT_SUCCESS,
      });
    } catch (error) {
      dispatch({
        type: LOGOUT_FAILED,
      });
    }
  };
};
