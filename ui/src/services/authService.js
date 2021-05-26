import 'cross-fetch/polyfill';

import { REGISTER_ERROR_MESSAGES, LOGIN_ERROR_MESSAGES, LOGOUT_ERROR_MESSAGES } from '../constants/messages';
import fetchResource from '../utils/fetchResource';

const authService = {
  register: ({ username, email, password }) =>
    fetchResource({
      method: 'POST',
      url: `${process.env.REACT_APP_API_URL}/auth/register`,
      requestBody: {
        username,
        email,
        password,
      },
      messageMapping: REGISTER_ERROR_MESSAGES,
    }),

  login: ({ username, password }) =>
    fetchResource({
      method: 'POST',
      url: `${process.env.REACT_APP_API_URL}/auth/login`,
      requestBody: {
        username,
        password,
      },
      messageMapping: LOGIN_ERROR_MESSAGES,
    }),

  logout: () =>
    fetchResource({
      method: 'POST',
      url: `${process.env.REACT_APP_API_URL}/auth/logout`,
      messageMapping: LOGOUT_ERROR_MESSAGES,
    }),
};

export default authService;
