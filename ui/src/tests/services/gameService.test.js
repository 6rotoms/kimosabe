import fetchMock from 'fetch-mock';

import gameService from '../../services/gameService';
import { SEARCH_ERROR_MESSAGES } from '../../constants';

describe('services/gameService.js', () => {
  afterEach(() => {
    fetchMock.restore();
  });

  describe('when search is called', () => {
    describe('and request has no errors', () => {
      let serviceResponse;
      beforeEach(async () => {
        // Arrange
        const response = {
          status: 200,
          body: {},
        };
        fetchMock.get(`${process.env.REACT_APP_API_URL}/games?searchTerm=test&pageNum=0`, response);

        // Act
        serviceResponse = await gameService.gameSearch({ searchTerm: 'test', pageNum: 0 });
      });

      test('then status 200 should be returned', () => {
        // Assert
        expect(serviceResponse).toEqual({ status: 200, body: {} });
      });
    });
  });
  describe('and request returns 500 error', () => {
    let serviceResponse;
    beforeEach(async () => {
      // Arrange
      const response = {
        status: 500,
      };
      fetchMock.get(`${process.env.REACT_APP_API_URL}/games?searchTerm=test&pageNum=0`, response);

      // Act
      serviceResponse = await gameService.gameSearch({ searchTerm: 'test', pageNum: 0 });
    });

    test('then status 500 should be returned', () => {
      // Assert
      expect(serviceResponse).toEqual({ status: 500, error: SEARCH_ERROR_MESSAGES[500] });
    });
  });
});
