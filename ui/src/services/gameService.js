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
  // eslint-disable-next-line no-unused-vars
  getSuggestions: async ({ searchTerm }) => {
    // TODO: Write gameSuggestions request function for real endpoint
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
  },
};

export default gameService;
