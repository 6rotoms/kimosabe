import React from 'react';
import { render, screen } from '../test-utils';
import { gameSearch, getSearchInfo } from '../../services/gameService';
import { makeStore } from '../../redux/store';
import SearchPage from '../../pages/search';
import { waitFor } from '@testing-library/dom';

jest.mock('../../services/gameService', () => ({
  gameSearch: jest.fn(),
  getSearchInfo: jest.fn(),
}));

describe('pages/search.js', () => {
  beforeEach(async () => {
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
        {
          coverUrl: 'test cover',
          id: 'super-long-summary',
          name: 'AAA',
          summary: 'a'.repeat(381),
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

    // Act
    gameSearch.mockReturnValueOnce(responseSearch);
    getSearchInfo.mockReturnValueOnce(responseSearchInfo);
    render(<SearchPage />, { store });
    await waitFor(() => {
      expect(gameSearch).toHaveBeenCalledTimes(1);
      expect(getSearchInfo).toHaveBeenCalledTimes(1);
    });
  });

  it('should render and display search results', () => {
    // Assert
    const searchResults = screen.getAllByTestId('search-result');
    expect(searchResults).toHaveLength(2);
    expect(screen.queryByText('Baldurs Test 3')).not.toBeNull();
    expect(screen.queryByText('test summary')).not.toBeNull();
    expect(screen.queryByText(`${'a'.repeat(377)}...`)).not.toBeNull();
  });
});
