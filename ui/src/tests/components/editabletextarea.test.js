import React from 'react';

import { render, screen } from '../test-utils';
import EditableTextArea from '../../components/EditableTextArea';
import { makeStore } from '../../redux/store';


describe('components/EditableTextArea.js', () => {
    let store;
    beforeEach(() => {
        store = makeStore();
    });
    describe('if EditableTextArea is not editable', () => {
        it('then edit button should not be present', () => {
            // Arrange
            // Act
            render(<EditableTextArea />, {store});

            // Assert
        });
    });
});