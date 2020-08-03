import React from 'react';
import { useStaticQuery } from 'gatsby';

import graphql from '../__mocks__/graphql';
import { render, fireEvent, screen, makeTestStore } from '../test-utils';
import LoginPage from '../../src/pages/login';
import * as actions from '../../src/redux/actions';

beforeEach(() => {
  useStaticQuery
    .mockImplementationOnce(graphql.layoutQuery)
    .mockImplementationOnce(graphql.SEOQuery);
});

describe('pages/login.js', () => {
  it('should render', () => {
    // Arrange
    const store = makeTestStore();

    // Act
    render(<LoginPage />, {store});

    // Assert
    expect(screen.getByTestId('username-field').value).toBe('');
    expect(screen.getByTestId('password-field').value).toBe('');
  });

  describe('when login button clicked', () => {
    beforeEach(() => {
        // eslint-disable-next-line no-import-assign
        actions.loginUser = jest.fn().mockImplementationOnce(() => ({type: 'logging in'}));
    });

    test('then loginUser action creater should be called', () => {
        // Arrange
        const store = makeTestStore();
        const expected = {type: 'logging in'};

        // Act
        render(<LoginPage />, {store});

        // Assert
        fireEvent.click(screen.getByText('Login'));
        expect(store.dispatch).toHaveBeenCalledWith(expected);
    });
  });
});
