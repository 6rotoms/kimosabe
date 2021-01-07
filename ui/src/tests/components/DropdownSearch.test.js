import React from 'react';
import DropdownSearch from '../../components/DropdownSearch';
import { render, screen, waitFor } from '../test-utils';
import { makeStore } from '../../redux/store';
import { fireEvent } from '@testing-library/react';

const suggestCallback = jest.fn();
const onSearch = jest.fn();
describe('components/DropdownSearch.js', () => {
  describe('when component loads', () => {
    beforeEach(() => {
      // Arrange
      const store = makeStore();

      // Act
      render(
          <DropdownSearch
              suggestionsCallback={suggestCallback}
          />,
          { store }
      );
    });

    it('it should render', () => {
      // Assert
      expect(screen.getByTestId('dropdown')).toBeInTheDocument();
    });

    it('it should not call suggestionsCallback', async () => {
      // Assert
      await waitFor(() => {
        expect(suggestCallback).not.toHaveBeenCalled();
      });
    });
  });

  describe('when dropdown is not in focus', () => {
    beforeEach(() => {});
  });

  describe('when dropdown is in focus', () => {
    beforeEach(() => {
      // Arrange
      const store = makeStore();

      // Act
      render(
          <DropdownSearch
              suggestionsCallback={suggestCallback}
              onSearch={onSearch}
          />,
          { store }
      );
      screen.getByTestId('searchbar').focus();
    });

    it('then suggestions should not show up', async () => {
      await waitFor(() => {
        expect(screen.queryByTestId('suggestions')).toBeNull();
      });
    });

    describe('and searchTerm is undefined', () => {
      let searchBar;
      beforeEach(() => {
        // Act
        searchBar = screen.getByTestId('searchbar');
        fireEvent.change(searchBar, { target: {value: undefined } });
      });

      it('then suggestions should not show up', async () => {
        // Assert
        await waitFor(() => {
          expect(screen.queryByTestId('suggestions')).toBeNull();
        });
      });
    });

    describe('and whitespace is entered', () => {
      let searchBar;
      beforeEach(() => {
        // Act
        searchBar = screen.getByTestId('searchbar');
        fireEvent.change(searchBar, { target: {value: '     ' } });
      });

      it('then value in input should match', () => {
        // Assert
        expect(searchBar.value).toBe('     ');
      });

      it('then suggestions should not show up', async () => {
        // Assert
        await waitFor(() => {
          expect(screen.queryByTestId('suggestions')).toBeNull();
        });
      });
    });

    describe('and text is entered', () => {
      let searchBar;
      beforeEach(() => {
        // Act
        suggestCallback.mockResolvedValueOnce([
          (<div key={1}>some-item</div>),
          (<div key={2}>some-item</div>),
          (<div key={3}>some-item</div>),
        ]);
        searchBar = screen.getByTestId('searchbar');
        fireEvent.change(searchBar, { target: {value: 'b' } });
      });

      it('then suggestions should show up', async () => {
        // Assert
        await waitFor(() => {
          expect(screen.queryByTestId('suggestions')).not.toBeNull();
        });
      });

      describe('and suggestCallback returns data', () => {
        beforeEach(() => {
        });

        it('then there should be the correct suggestions', async () => {
          // Assert
          const elements = await screen.findAllByText('some-item');
          expect(elements).toHaveLength(3);
        });

        describe('and searchbar gets unfocused', () => {
          it('then suggestions should hide', async () => {
            // Act
            fireEvent.blur(screen.getByTestId('dropdown'));

            // Assert
            await waitFor(() => {
              expect(screen.queryByTestId('suggestions')).toBeNull();
            });
          });
        });
      });
    });

    describe('and enter key is pressed', () => {
      beforeEach(() => {
        // Act
        const searchBar = screen.getByTestId('searchbar');
        fireEvent.keyUp(searchBar, { key: 'Enter', code: 13, keyCode: 13});
      });

      it('then onSearch should fire', () => {
        // Assert
        expect(onSearch).toHaveBeenCalledTimes(1);
      });
    });
  });
});