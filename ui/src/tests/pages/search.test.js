import React from 'react';
import { render, fireEvent, screen } from '../test-utils';
import { gameSearch } from '../../services/gameService';
import Header from '../../components/Header';
import { makeStore } from '../../redux/store';
import SearchPage from '../../pages/search';
import history from '../../history';

jest.mock('../../services/gameService', () => ({
  gameSearch: jest.fn(),
}));

jest.spyOn(history, 'push');

describe('components/header.js', () => {
  it('should render', () => {
    // Arrange
    const store = makeStore();
    // Act
    render(<Header />, { store });

    // Assert
    expect(screen.getByTestId('header-search').value).toBe('');
  });

  describe('when search term is changed', () => {
    test('then state should change', async () => {
      // Arrange
      const store = makeStore();

      // Act
      const { findByTestId } = render(<Header />, { store });
      const field = await findByTestId('header-search');
      fireEvent.change(field, { target: { value: 'test' } });

      // Assert
      expect(screen.getByTestId('header-search').value).toBe('test');
    });
  });

  describe('when enter is pressed,', () => {
    test('window location should be assigned a new url', async () => {
      // Arrange
      const store = makeStore();

      // Act
      const { findByTestId } = render(<Header />, { store });
      const field = await findByTestId('header-search');
      fireEvent.change(field, { target: { value: 'test' } });
      fireEvent.keyUp(field, { key: 'Enter', code: 'Enter', keyCode: 13 });

      // Assert
      expect(history.push).toHaveBeenCalled();
    });
  });
});

describe('pages/search.js', () => {
  it('should render and display search results', async () => {
    // Arrange
    const store = makeStore();
    const response = {
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
    gameSearch.mockReturnValueOnce(response);
    // Act
    const { findByTestId } = render(<SearchPage />, { store });
    const searchResults = await findByTestId('search-results');
    // Assert
    expect(searchResults.children).toHaveLength(1);
  });
});
