import 'cross-fetch/polyfill';

import { SEARCH_ERROR_MESSAGES } from '../constants/auth';

const gameService = {
  gameSearch: async ({ searchTerm, pageNum }) => {
    try {
      const response = await fetch(
        `${process.env.GATSBY_API_URL}/games?searchTerm=${searchTerm}&pageNum=${pageNum}`,
        {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
        }
      );

      const body = await response.json();

      return {
        status: 200,
        body: body,
      };
    } catch (error) {
      return {
        status: 500,
        error: SEARCH_ERROR_MESSAGES.UNDEFINED,
      };
    }
  },
};

export default gameService;
