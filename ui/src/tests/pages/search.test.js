import React from 'react';
import { render, fireEvent, screen } from '../test-utils';
import Header from '../../components/header';
import { makeStore } from '../../redux/store';

describe('components/header.js', () => {
  beforeAll(() => {
    delete window.location;
    window.location = { assign: jest.fn() };
  });

  afterAll(() => {
    window.location = location;
  });

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
      expect(window.location.assign).toHaveBeenCalled();
    });
  });
});
