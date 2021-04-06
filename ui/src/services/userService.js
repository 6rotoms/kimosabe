import { USER_PROFILE_MESSAGES } from '../constants';
import fetchResource from '../utils/fetchResource';

const userService = {
  getFriends: ({ username }) =>
    fetchResource({
      method: 'GET',
      url: `${process.env.REACT_APP_API_URL}/user/profile/${username}/friends`,
      messageMapping: USER_PROFILE_MESSAGES,
    }),

  getGroups: ({ username }) =>
    fetchResource({
      method: 'GET',
      url: `${process.env.REACT_APP_API_URL}/user/profile/${username}/groups`,
      messageMapping: USER_PROFILE_MESSAGES,
    }),

  getUserData: ({ username }) =>
    fetchResource({
      method: 'GET',
      url: `${process.env.REACT_APP_API_URL}/user/profile/${username}`,
      messageMapping: USER_PROFILE_MESSAGES,
    }),
};

export default userService;
