import 'cross-fetch/polyfill';

import { SEARCH_ERROR_MESSAGES } from '../constants/game';

const gameService = {
  gameSearch: async ({ searchTerm, pageNum }) => {
    try {
      const response = await fetch(
        `${process.env.REACT_APP_API_URL}/games?searchTerm=${encodeURIComponent(searchTerm)}&pageNum=${pageNum}`,
        {
          method: 'GET',
        },
      );

      const body = await response.json();
      return {
        status: response.status,
        body: body,
      };
    } catch (error) {
      return {
        status: 500,
        error: SEARCH_ERROR_MESSAGES[500],
      };
    }
  },

  getSearchInfo: async ({ searchTerm }) => {
    try {
      const response = await fetch(
        `${process.env.REACT_APP_API_URL}/games/searchInfo?searchTerm=${encodeURIComponent(searchTerm)}`,
        {
          method: 'GET',
        },
      );

      const body = await response.json();

      return {
        status: response.status,
        body: body,
      };
    } catch (error) {
      return {
        status: 500,
        error: SEARCH_ERROR_MESSAGES[500],
      };
    }
  },
};

export default gameService;
