import React from 'react';
import { render, fireEvent, screen, waitFor } from '../test-utils';
import UserProfilePage from '../../pages/userprofile';
import { getGroups, getUserData, getFriends } from '../../services/userService';
import { Route } from 'react-router';

jest.mock('../../history', () => ({
  push: jest.fn(),
}));

jest.mock('../../services/userService', () => ({
  getGroups: jest.fn(),
  getFriends: jest.fn(),
  getUserData: jest.fn(),
}));

const mockWithGoodResponse = () => {
  const response = {
    status: 200,
    body: [
      {
        coverUrl: '//images.igdb.com/igdb/image/upload/t_cover_big/co1ndn.jpg',
        groupName: 'Baldur’s Gate I: Enhanced Edition',
        groupId: 'some-string1',
      },
      {
        coverUrl: '//images.igdb.com/igdb/image/upload/t_cover_big/co1ndn.jpg',
        groupName: 'Really Long Title here to test flexbox and overlap',
        groupId: 'some-string2',
      },
      {
        coverUrl: '//images.igdb.com/igdb/image/upload/t_cover_big/co1ndn.jpg',
        groupName: 'bg3',
        groupId: 'some-string3',
      },
    ],
  };
  getGroups.mockResolvedValueOnce(response);
};

const mockWithBadResponse = () => {
  const response = {
    status: 500,
  };
  getGroups.mockResolvedValueOnce(response);
};

describe('pages/userprofile.js', () => {
  beforeEach(() => {
    // Arrange
    getUserData.mockResolvedValue({
      status: 200,
      body: {
        age: '18',
        gender: 'Female',
        location: 'California',
        lastLogin: '2021-04-02T03:02:18.229798Z',
        biography: 'some bio here',
      },
    });
    getFriends.mockResolvedValue({
      status: 200,
      body: [{ username: 'user2' }],
    });
  });

  afterEach(() => {
    jest.resetAllMocks();
  });

  describe('and apis all return 200', () => {
    beforeEach(() => {
      mockWithGoodResponse();
    });
    it('it should render', async () => {
      // Act
      render(
        <Route path="/users/:username">
          <UserProfilePage />
        </Route>,
        { route: '/users/user1' },
      );

      // Assert
      await waitFor(() => {
        expect(screen.queryByTestId('users-friends')).not.toBeNull();
        expect(screen.queryByText('user1')).not.toBeNull();
        const expectedDateString = new Date('2021-04-02T03:02:18.229798Z').toDateString();
        expect(screen.queryByText(new RegExp(expectedDateString))).not.toBeNull();
        expect(screen.queryByText('18')).not.toBeNull();
        expect(screen.queryByText('Female')).not.toBeNull();
        expect(screen.queryByText('California')).not.toBeNull();
        expect(screen.queryByText('some bio here')).not.toBeNull();
      });
    });

    describe('when user clicks group', () => {
      it('then it should display groups tab', async () => {
        // Act
        render(
          <Route path="/users/:username">
            <UserProfilePage />
          </Route>,
          { route: '/users/user1' },
        );
        fireEvent.click(screen.getByText('Groups'));
        // Assert
        await waitFor(() => {
          expect(screen.queryByTestId('users-groups')).not.toBeNull();
        });
      });

      it('then it should redirect to group', async () => {
        // Act
        render(
          <Route path="/users/:username">
            <UserProfilePage />
          </Route>,
          { route: '/users/user1' },
        );
        fireEvent.click(screen.getByText('Groups'));
        // Assert
        await waitFor(() => {
          expect(screen.getByTestId('users-groups').children[0].getAttribute('href')).toBe('/group/some-string1');
        });
      });
    });
  });

  describe('when group api call returns non 200 status code', () => {
    it('then no groups should be listed', async () => {
      // Arrange
      mockWithBadResponse();

      // Act
      render(
        <Route path="/users/:username">
          <UserProfilePage />
        </Route>,
        { route: '/users/user1' },
      );
      fireEvent.click(screen.getByText('Groups'));

      // Assert
      await waitFor(() => {
        expect(screen.queryByTestId('users-groups').children).toHaveLength(0);
      });
    });
  });
});
