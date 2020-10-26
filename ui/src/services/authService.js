import 'cross-fetch/polyfill';

import { REGISTER_ERROR_MESSAGES, LOGIN_ERROR_MESSAGES, LOGOUT_ERROR_MESSAGES } from '../constants/auth';

const authService = {
  register: async ({ username, password }) => {
    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/auth/register`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          username,
          password,
        }),
      });
      if (response.status !== 200) {
        return {
          status: response.status,
          error: REGISTER_ERROR_MESSAGES[response.status] || REGISTER_ERROR_MESSAGES.UNDEFINED,
        };
      }
      return { status: response.status };
    } catch (error) {
      return {
        status: 500,
        error: REGISTER_ERROR_MESSAGES.UNDEFINED,
      };
    }
  },

  login: async ({ username, password }) => {
    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/auth/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          username,
          password,
        }),
      });
      if (response.status !== 200) {
        return {
          status: response.status,
          error: LOGIN_ERROR_MESSAGES[response.status] || LOGIN_ERROR_MESSAGES.UNDEFINED,
        };
      }
      return { status: response.status };
    } catch (error) {
      console.log(error);
      return {
        status: 500,
        error: LOGIN_ERROR_MESSAGES.UNDEFINED,
      };
    }
  },

  logout: async () => {
    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/auth/logout`, {
        method: 'POST',
      });
      if (response.status !== 200) {
        return {
          status: response.status,
          error: LOGOUT_ERROR_MESSAGES[response.status] || LOGOUT_ERROR_MESSAGES.UNDEFINED,
        };
      }
      return { status: response.status };
    } catch (error) {
      return {
        status: 500,
        error: LOGOUT_ERROR_MESSAGES.UNDEFINED,
      };
    }
  },
};

export default authService;
