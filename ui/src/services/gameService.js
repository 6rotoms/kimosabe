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
        body,
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
        body,
      };
    } catch (error) {
      return {
        status: 500,
        error: SEARCH_ERROR_MESSAGES[500],
      };
    }
  },

  getSuggestions: async ({ searchTerm }) => {
    try {
      // TODO: Write gameSuggestions request function for real endpoint
      console.log(searchTerm);
      return {
        status: 200,
        body: [
          {
            name: 'baldurs gate',
            coverUrl: 'https://path.to.file.com',
            thumbUrl: '//images.igdb.com/igdb/image/upload/t_thumb/co1ndn.jpg',
            summary: 'info about baldurs gate here',
            id: 'baldurs-gate-3',
          },
          {
            name: 'baldurs gate',
            coverUrl: 'https://path.to.file.com',
            thumbUrl: '//images.igdb.com/igdb/image/upload/t_thumb/co1ndn.jpg',
            summary: 'info about baldurs gate here',
            id: 'baldurs-gate-2',
          },
          {
            name: 'baldurs gate',
            coverUrl: 'https://path.to.file.com',
            thumbUrl: '//images.igdb.com/igdb/image/upload/t_thumb/co1ndn.jpg',
            summary: 'info about baldurs gate here',
            id: 'baldurs-gate-1',
          },
        ],
      };
    } catch (error) {
      return {
        status: 500,
      };
    }
  },
};

export default gameService;
