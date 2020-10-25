import React from 'react';

import { render, screen } from '../test-utils';
import Tile from '../../components/Tile';
import { makeStore } from '../../redux/store';

describe('components/Tile.js', () => {
  let store;
  beforeEach(() => {
    store = makeStore();
  });
  describe('when title is not specified', () => {
    it('then there should be no title', () => {
      // Arrange
      // Act
      render(<Tile />, { store });

      // Assert
      const title = screen.queryByTestId('title-div');
      expect(title).not.toBeInTheDocument();
    });
  });
  describe('when title is specified', () => {
    it('then title should be shown', () => {
      // Arrange
      // Act
      render(<Tile title="title" />, { store });

      // Assert
      const title = screen.queryByTestId('title-div');
      expect(title).toBeInTheDocument();
    });

    describe('and alignment is not set', () => {
      it('then it should default to center', () => {
        // Arrange
        // Act
        render(<Tile title="title" />, { store });

        // Assert
        const title = screen.queryByTestId('title-content');
        expect(title).toHaveStyle({ 'text-align': 'center' });
      });
    });
    describe.each(['left', 'center', 'right'])('and alignment is set to %s', (alignment) => {
      it(`then title should follow alignment ${alignment}`, () => {
        // Arrange
        // Act
        render(<Tile title="title" titleAlign={alignment} />, { store });

        // Assert
        const title = screen.queryByTestId('title-content');
        expect(title).toHaveStyle({ 'text-align': alignment });
      });
    });
  });
});
