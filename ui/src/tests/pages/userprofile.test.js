import React from 'react';
import { render, fireEvent, screen, waitFor } from '../test-utils';
import history from '../../history';
import UserProfilePage from '../../pages/userprofile';
import { getGroups } from '../../services/userService';
import { makeStore } from '../../redux/store';

jest.mock('../../history', () => ({
  push: jest.fn(),
}));

jest.mock('../../services/userService', () => ({
  getGroups: jest.fn(),
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
  getGroups.mockReturnValueOnce(response);
};

const mockWithEllipseResponse = () => {
  const response = {
    status: 200,
    body: [
      {
        coverUrl: '//images.igdb.com/igdb/image/upload/t_cover_big/co1ndn.jpg',
        groupName: '0123456789a123456789b123456789c1234',
        groupId: 'some-string5',
      },
    ],
  };
  getGroups.mockReturnValueOnce(response);
};

const mockWithBadResponse = () => {
  const response = {
    status: 500,
  };
  getGroups.mockReturnValueOnce(response);
};

describe('pages/userprofile.js', () => {
  it('it should render', async () => {
    // Arrange
    mockWithGoodResponse();
    const store = makeStore();

    // Act
    render(<UserProfilePage />, { store });

    // Assert
    await waitFor(() => {});
    expect(screen.getByTestId('users-groups').children).toHaveLength(3);
  });

  describe('when user clicks group', () => {
    it('then it should redirect to group', async () => {
      // Arrange
      mockWithGoodResponse();
      const store = makeStore();

      // Act
      render(<UserProfilePage />, { store });

      // Assert
      await waitFor(() => {});
      fireEvent.click(screen.getByTestId('users-groups').children[0]);
      expect(history.push).toHaveBeenCalledWith('/group/some-string1');
    });
  });

  describe('when group name longer than 34 characters', () => {
    it('then ellipses should be added at end', async () => {
      // Arrange
      mockWithEllipseResponse();
      const store = makeStore();

      // Act
      render(<UserProfilePage />, { store });

      // Assert
      await waitFor(() => {});
      expect(screen.getByText('0123456789a123456789b123456789c1...')).not.toEqual(null);
    });
  });

  describe('when group api call returns non 200 status code', () => {
    it('then no groups should be listed', async () => {
      // Arrange
      mockWithBadResponse();
      const store = makeStore();

      // Act
      render(<UserProfilePage />, { store });

      // Assert
      expect(screen.getByTestId('users-groups').children).toHaveLength(0);
    });
  });
});
