import React from 'react';
import { Layout } from '../../components';
import { render, fireEvent, screen } from '../test-utils';
import { login } from '../../services/authService';
import { getSuggestions } from '../../services/gameService';
import { loginUser } from '../../redux/actions/authActions';
import history from '../../history';
import { waitFor } from '@testing-library/react';
import { makeStore } from '../../redux/store';

jest.spyOn(history, 'push');
jest.mock('../../services/authService', () => ({
  login: jest.fn(),
}));

jest.mock('../../services/gameService', () => ({
  getSuggestions: jest.fn(),
}));

describe('components/layout.js', () => {
  it('should render', async () => {
    // Act
    render(<Layout />);

    // Assert
    await waitFor(() => {
      expect(screen.queryByTestId('header-search')).not.toBeNull();
      expect(screen.queryByTestId('header-login')).not.toBeNull();
    });
  });

  describe('when search term is changed', () => {
    let field;
    beforeEach(async () => {
      // Arrange
      const responseSuggestions = {
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

      // Act
      getSuggestions.mockReturnValueOnce(responseSuggestions);
      const { findByTestId } = render(<Layout />);
      field = await findByTestId('header-search');
      field.focus();
      fireEvent.change(field, { target: { value: 'test' } });
    });

    it('then state should change', () => {
      // Assert
      expect(screen.getByTestId('header-search').value).toBe('test');
    });

    it('then suggestions should show up', async () => {
      // Assert
      await waitFor(() => {
        expect(getSuggestions).toHaveBeenCalledTimes(1);
      });
      const suggestions = screen.getAllByTestId('suggestion');
      expect(suggestions).toHaveLength(1);
      expect(screen.getByText('Baldurs Test 3')).not.toBeNull();
    });

    describe('and enter is pressed', () => {
      it('then window location should be assigned a new url', async () => {
        // Act
        fireEvent.keyUp(field, { key: 'Enter', code: 'Enter', keyCode: 13 });

        // Assert
        expect(history.push).toHaveBeenCalled();
      });
    });
  });

  describe('when user is not logged in', () => {
    it('then login should be displayed', () => {
      // Act
      render(<Layout />);
      const loginButton = screen.queryByText('Login');

      // Assert
      expect(loginButton).not.toBeNull();
    });
  });

  describe('when user is logged in', () => {
    let store;
    beforeEach(async () => {
      // Arrange
      store = makeStore();
      const response = {
        status: 200,
      };
      login.mockReturnValueOnce(response);
      await store.dispatch(loginUser({ username: 'any', password: 'thing' }));
    });

    it('then logout should be displayed', async () => {
      // Act
      const { queryByText } = render(<Layout />, { store });
      const loginButton = queryByText('Logout');

      // Assert
      expect(loginButton).not.toBeNull();
    });
  });
});
