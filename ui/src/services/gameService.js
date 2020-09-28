import 'cross-fetch/polyfill';

import { SEARCH_ERROR_MESSAGES } from '../constants/game';

const gameService = {
  gameSearch: async ({ searchTerm, pageNum }) => {
    try {
      console.log(`${process.env.REACT_APP_API_URL}/games?searchTerm=${searchTerm}&pageNum=${pageNum}`);
      const response = await fetch(
        `${process.env.REACT_APP_API_URL}/games?searchTerm=${searchTerm}&pageNum=${pageNum}`,
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
