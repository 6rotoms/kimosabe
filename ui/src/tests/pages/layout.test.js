import React from 'react';
import { Layout } from '../../components';
import { render, fireEvent, screen } from '../test-utils';
import { getSuggestions } from '../../services/gameService';
import { logout } from '../../services/authService';
import { createMemoryHistory } from 'history';
import { waitFor } from '@testing-library/react';

jest.mock('../../services/gameService', () => ({
  getSuggestions: jest.fn(),
}));

jest.mock('../../services/authService', () => ({
  logout: jest.fn(),
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
    let history;
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
      history = createMemoryHistory();
      jest.spyOn(history, 'push');

      const { findByTestId } = render(<Layout />, { history });
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
    let history;
    beforeEach(() => {
      // Arrange
      history = createMemoryHistory();
      jest.spyOn(history, 'push');
      render(<Layout />, { history });
    });
    it('then login should be displayed', () => {
      // Act
      const loginButton = screen.queryByText('Login');

      // Assert
      expect(loginButton).not.toBeNull();
    });

    it('then clicking login should direct to login', async () => {
      // Act
      const loginButton = screen.queryByText('Login');
      fireEvent.click(loginButton);
      await waitFor(() => {
        expect(history.push).toHaveBeenCalledWith('/login/');
      });
    });
  });

  describe('when user is logged in', () => {
    beforeEach(async () => {
      // Arrange
      render(<Layout />, { initialState: { loggedIn: true } });
    });

    it('then logout should be displayed', async () => {
      // Act
      const logoutButton = screen.queryByText('Logout');

      // Assert
      expect(logoutButton).not.toBeNull();
    });

    it('then clicking logout should logout user', async () => {
      // Act
      const logoutButton = screen.queryByText('Logout');
      logout.mockResolvedValue({ status: 200 });
      fireEvent.click(logoutButton);
      await waitFor(() => {
        const loginButton = screen.queryByText('Login');
        expect(loginButton).not.toBeNull();
      });
    });
  });
});
