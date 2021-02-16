import React from 'react';
import { within, render, fireEvent, screen, waitFor } from '../test-utils';
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

const mockWithBadResponse = () => {
  const response = {
    status: 500,
  };
  getGroups.mockReturnValueOnce(response);
};

describe('pages/userprofile.js', () => {
  let store;
  beforeEach(() => {
    // Arrange
    mockWithGoodResponse();
    store = makeStore();
  });

  afterEach(() => {
    jest.resetAllMocks();
  });

  it('it should render', async () => {
    // Act
    render(<UserProfilePage />, { store });

    // Assert
    await waitFor(() => {
      expect(screen.queryByTestId('users-friends')).not.toBeNull();
      expect(screen.queryByText('Edit')).not.toBeNull();
      expect(screen.queryByText('Save')).toBeNull();
      expect(screen.queryByText('Cancel')).toBeNull();
    });
  });

  describe('when user clicks edit user bio', () => {
    beforeEach(async () => {
      // Act
      await waitFor(() => {
        render(<UserProfilePage />, { store });
        fireEvent.click(screen.getByText('Edit'));
      });
    });
    it('then textarea should be editable', async () => {
      // Assert
      await waitFor(() => {
        expect(screen.queryByText('Edit')).toBeNull();
        expect(screen.queryByText('Save')).not.toBeNull();
        expect(screen.queryByText('Cancel')).not.toBeNull();
      });
    });

    describe('and user edits bio', () => {
      it('then value should update on save', async () => {
        const textElement = within(screen.getByTestId('user-bio')).getByRole('textbox');
        fireEvent.change(textElement, { target: { value: 'new bio' } });
        fireEvent.click(screen.getByText('Save'));
        await waitFor(() => {
          expect(screen.queryByText('new bio')).not.toBeNull();
        });
      });
      it('then value should not persist on cancel', async () => {
        const textElement = within(screen.getByTestId('user-bio')).getByRole('textbox');
        fireEvent.change(textElement, { target: { value: 'new bio' } });
        fireEvent.click(screen.getByText('Cancel'));
        await waitFor(() => {
          expect(screen.queryByText('new bio')).toBeNull();
        });
      });
    });
  });

  describe('when user clicks group', () => {
    it('then it should display groups tab', async () => {
      // Act
      render(<UserProfilePage />, { store });
      fireEvent.click(screen.getByText('Groups'));
      // Assert
      await waitFor(() => {
        expect(screen.queryByTestId('users-groups')).not.toBeNull();
      });
    });

    it('then it should redirect to group', async () => {
      // Act
      render(<UserProfilePage />, { store });
      fireEvent.click(screen.getByText('Groups'));
      // Assert
      await waitFor(() => {
        expect(screen.getByTestId('users-groups').children[0].getAttribute('href')).toBe('/group/some-string1');
      });
    });
  });

  describe('when group api call returns non 200 status code', () => {
    it('then no groups should be listed', async () => {
      // Arrange
      jest.resetAllMocks();
      mockWithBadResponse();

      // Act
      render(<UserProfilePage />, { store });
      fireEvent.click(screen.getByText('Groups'));

      // Assert
      await waitFor(() => {
        expect(screen.queryByTestId('users-groups').children).toHaveLength(0);
      });
    });
  });
});
