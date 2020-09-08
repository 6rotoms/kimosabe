export const REGISTER_ERROR_MESSAGES = {
  409: 'Username Taken',
  403: 'Invalid Username or Password',
  500: 'Internal Server Error, Please try again Later',
  'USERNAME_TOO_SHORT': 'Username should be at least 3 characters',
  'PASSWORD_TOO_SHORT': 'Password should be at least 3 characters',
  'CONFIRM_PASSWORD_FAILED': 'Passwords should match',
  UNDEFINED: 'Something went wrong.',
};

export const LOGIN_ERROR_MESSAGES = {
  403: 'Username or Password Incorrect',
  500: 'Internal Server Error, Please try again Later',
  UNDEFINED: 'Something went wrong.',
};

export const LOGOUT_ERROR_MESSAGES = {
  500: 'Internal Server Error, Please try again Later',
  UNDEFINED: 'Something went wrong.',
};