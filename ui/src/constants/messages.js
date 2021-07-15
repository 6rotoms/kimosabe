export const UNDEFINED_ERROR_MESSAGE = 'Something went wrong';

export const REGISTER_ERROR_MESSAGES = {
  400: 'Bad Request',
  401: 'Invalid Username or Password',
  409: 'Username Taken',
  500: 'Internal Server Error, Please try again Later',
  USERNAME_TOO_SHORT: 'Username should be at least 3 characters',
  PASSWORD_TOO_SHORT: 'Password should be at least 3 characters',
  CONFIRM_PASSWORD_FAILED: 'Passwords should match',
};

export const LOGIN_ERROR_MESSAGES = {
  401: 'Username or Password Incorrect',
  500: 'Internal Server Error, Please try again Later',
};

export const LOGOUT_ERROR_MESSAGES = {
  500: 'Internal Server Error, Please try again Later',
};

export const VERIFY_ERROR_MESSAGES = {
  404: 'Validation token already used.',
  500: 'Internal Server Error, Please try again Later',
};

export const SEARCH_ERROR_MESSAGES = {
  500: 'Internal Server Error, Please try again Later',
  400: 'Bad Request. Something went wrong.',
};

export const USER_PROFILE_MESSAGES = {
  500: 'Internal Server Error, Please try again Later',
  404: 'User does not exist',
};
