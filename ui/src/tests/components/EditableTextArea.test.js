import React from 'react';

import { render, screen, fireEvent } from '../test-utils';
import EditableTextArea from '../../components/EditableTextArea';
import { makeStore } from '../../redux/store';

describe('components/EditableTextArea.js', () => {
  let store;
  let onSaveFunction;
  beforeEach(() => {
    onSaveFunction = jest.fn();
    store = makeStore();
  });
  describe('if isToggleable is not set', () => {
    it('then buttons should not be present', () => {
      // Arrange
      // Act
      render(<EditableTextArea onSave={onSaveFunction} />, { store });

      // Assert
      const buttons = screen.queryByTestId('button-container');
      expect(buttons).not.toBeInTheDocument();
    });
  });
  describe('if isToggleable is set', () => {
    it('then buttons should be present', async () => {
      // Arrange
      // Act
      render(<EditableTextArea onSave={onSaveFunction} isToggleable />, { store });

      // Assert
      const buttons = screen.queryByTestId('button-container');
      const editButton = await screen.findByText('Edit');
      expect(buttons).toBeInTheDocument();
      expect(editButton).toBeInTheDocument();
    });

    describe('and edit button is clicked', () => {
      beforeEach(() => {
        // Arrange
        // Act
        render(<EditableTextArea onSave={onSaveFunction} isToggleable />, { store });
        fireEvent.click(screen.getByText('Edit'));
      });

      it('then edit and save button should be present', async () => {
        // Assert
        const editButton = await screen.findByText('Save');
        const cancelButton = await screen.findByText('Cancel');
        expect(editButton).toBeInTheDocument();
        expect(cancelButton).toBeInTheDocument();
      });

      it('then textarea should be present', async () => {
        // Assert
        const textArea = screen.getByTestId('display-textarea');
        expect(textArea).toBeInTheDocument();
      });

      describe('and textarea is typed in', () => {
        const newText = 'this is the new text';
        beforeEach(() => {
          // Arrange
          // Act
          const textArea = screen.getByTestId('display-textarea');
          fireEvent.change(textArea, { target: { value: newText } });
        });

        describe('and save button is pressed', () => {
          beforeEach(() => {
            // Act
            // Arrange
            fireEvent.click(screen.getByText('Save'));
          });

          it('then div should contain new text', () => {
            // Assert
            const text = screen.getByTestId('display-text');
            expect(text).toHaveTextContent(newText);
          });

          it('then onSave function should be called', () => {
            // Assert
            expect(onSaveFunction).toHaveBeenCalledWith(newText);
          });
        });

        describe('and cancel button is pressed', () => {
          beforeEach(() => {
            // Act
            // Arrange
            fireEvent.click(screen.getByText('Cancel'));
          });

          it('then div should contain new text', () => {
            // Assert
            const text = screen.getByTestId('display-text');
            expect(text).toHaveTextContent('');
          });

          it('then onSave function should be called', () => {
            // Assert
            expect(onSaveFunction).toHaveBeenCalledTimes(0);
          });
        });
      });
    });
  });

  describe('if initialText is set', () => {
    it('then text should show up in div', () => {
      // Arrange
      const initialText = 'Initial Text';
      // Act
      render(<EditableTextArea onSave={onSaveFunction} initialText={initialText} />, { store });
      const text = screen.getByTestId('display-text');
      expect(text).toHaveTextContent(initialText);
    });
  });

  describe('if charLimit is set to non-zero positive number', () => {
    it('then text should be limited to n chars', () => {
      // Arrange
      const charLimit = 2;
      const initialText = 'Initial Text';
      // Act
      render(<EditableTextArea onSave={onSaveFunction} initialText={initialText} charLimit={charLimit} />, { store });
      const text = screen.getByTestId('display-text');
      expect(text).toHaveTextContent(initialText.substring(0, 2));
    });
  });

  describe('if charLimit is set to number <= 0', () => {
    it('then text should not be limited', () => {
      // Arrange
      const charLimit = 0;
      const initialText = 'Initial Text';
      // Act
      render(<EditableTextArea onSave={onSaveFunction} initialText={initialText} charLimit={charLimit} />, { store });
      const text = screen.getByTestId('display-text');
      expect(text).toHaveTextContent(initialText);
    });
  });
});
