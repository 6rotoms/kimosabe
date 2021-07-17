// test-utils.js
import React from 'react';
import { render as rtlRender } from '@testing-library/react';
import { Router } from 'react-router-dom';
import { createMemoryHistory } from 'history';
import { AppProvider } from '../context/AppContext';

const TestWrapper = ({ initialState, history, children }) => (
  <AppProvider initialState={initialState}>
    <Router history={history}>{children}</Router>
  </AppProvider>
);

function render(
  ui,
  { route = '/', history = createMemoryHistory({ initialEntries: [route] }), initialState, ...otherOpts } = {},
) {
  return rtlRender(
    <TestWrapper history={history} initialState={initialState}>
      {ui}
    </TestWrapper>,
    otherOpts,
  );
}

// re-export everything
export * from '@testing-library/react';
// override render method
export { render };
