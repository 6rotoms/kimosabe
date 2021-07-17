// test-utils.js
import React from 'react';
import { render as rtlRender } from '@testing-library/react';
import { Router } from 'react-router-dom';
import { createMemoryHistory } from 'history';

const TestWrapper = ({ history, children }) => <Router history={history}>{children}</Router>;

function render(ui, { route = '/', history = createMemoryHistory({ initialEntries: [route] }), ...otherOpts } = {}) {
  return rtlRender(<TestWrapper history={history}>{ui}</TestWrapper>, otherOpts);
}

// re-export everything
export * from '@testing-library/react';
// override render method
export { render };
