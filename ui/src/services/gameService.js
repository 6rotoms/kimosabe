import 'cross-fetch/polyfill';

import { SEARCH_ERROR_MESSAGES } from '../constants';
import fetchResource from '../utils/fetchResource';

const gameService = {
  gameSearch: ({ searchTerm, pageNum }) =>
    fetchResource({
      method: 'GET',
      url: `${process.env.REACT_APP_API_URL}/games?searchTerm=${encodeURIComponent(searchTerm)}&pageNum=${pageNum}`,
      messageMapping: SEARCH_ERROR_MESSAGES,
    }),

  getSearchInfo: ({ searchTerm }) =>
    fetchResource({
      method: 'GET',
      url: `${process.env.REACT_APP_API_URL}/games/searchInfo?searchTerm=${encodeURIComponent(searchTerm)}`,
      messageMapping: SEARCH_ERROR_MESSAGES,
    }),
  getSuggestions: async ({ searchTerm }) =>
    fetchResource({
      method: 'GET',
      url: `${process.env.REACT_APP_API_URL}/games?searchTerm=${encodeURIComponent(searchTerm)}&pageSize=3`,
      messageMapping: SEARCH_ERROR_MESSAGES,
    }),
};

export default gameService;
