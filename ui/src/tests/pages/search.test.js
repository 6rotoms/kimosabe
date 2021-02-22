import React from 'react';
import { render } from '../test-utils';
import { gameSearch, getSearchInfo } from '../../services/gameService';
import { makeStore } from '../../redux/store';
import SearchPage from '../../pages/search';
import history from '../../history';

jest.mock('../../services/gameService', () => ({
  gameSearch: jest.fn(),
  getSearchInfo: jest.fn(),
}));

jest.spyOn(history, 'push');

describe('pages/search.js', () => {
  it('should render and display search results', async () => {
    // Arrange
    const store = makeStore();
    const responseSearch = {
      status: 200,
      body: [
        {
          coverUrl: 'test cover',
          id: 'baldurs-test-3',
          name: 'Baldurs Test 3',
          summary: 'test summary',
          thumbUrl: 'test thumb',
        },
      ],
    };

    const responseSearchInfo = {
      status: 200,
      body: [
        {
          maxNumPages: 0,
          numSearchResults: 1,
          searchTerm: 'test',
        },
      ],
    };

    gameSearch.mockReturnValueOnce(responseSearch);
    getSearchInfo.mockReturnValueOnce(responseSearchInfo);

    // Act
    const { findByTestId } = render(<SearchPage />, { store });
    const searchResults = await findByTestId('search-results');
    // Assert
    expect(searchResults.children).toHaveLength(2);
  });
});
