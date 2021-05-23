import React from 'react';
import { VerifyPage } from '../../pages';
import { render, screen, waitFor } from '../test-utils';
import { verify } from '../../services/authService';
import { VERIFY_ERROR_MESSAGES } from '../../constants';

const mockHistoryPush = jest.fn();
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useHistory: () => ({
    push: mockHistoryPush,
  }),
}));
jest.mock('../../services/authService', () => ({
  verify: jest.fn(),
}));

const mockWithGoodResponse = () => {
  const response = {
    status: 200,
  };
  verify.mockResolvedValueOnce(response);
};

const mockWithBadResponse = () => {
  const response = {
    status: 404,
    error: VERIFY_ERROR_MESSAGES[404],
  };
  verify.mockResolvedValueOnce(response);
};

describe('pages/verify.js', () => {
  describe('when verify succeeds', () => {
    beforeEach(() => {
      // Arrange
      mockWithGoodResponse();
    });

    it('should Render', async () => {
      // Act
      render(<VerifyPage token="token" />);

      // Assert
      expect(screen.queryByText('Loading...')).not.toBeNull();
      await waitFor(() => {
        expect(screen.queryByText(/Verification Success/i)).not.toBeNull();
        expect(screen.queryByText(/Go to login/i)).not.toBeNull();
      });
    });
  });

  describe('when verify fails', () => {
    beforeEach(() => {
      // Arrange
      mockWithBadResponse();
    });

    it('should Render', async () => {
      // Act
      const { queryByText } = render(<VerifyPage token="token" />);

      // Assert
      expect(queryByText('Loading...')).not.toBeNull();
      await waitFor(() => {
        expect(queryByText(VERIFY_ERROR_MESSAGES[404])).not.toBeNull();
        expect(queryByText(/Go to Homepage/i)).not.toBeNull();
      });
    });
  });
});
