// test-utils.js
import React from 'react';
import { render as rtlRender } from '@testing-library/react';
import { Provider } from 'react-redux';
import { Router } from 'react-router-dom';
import { createMemoryHistory } from 'history';
import { makeStore } from '../redux/store';

const TestWrapper = ({ history, store, children }) => (
  <Provider store={store}>
    <Router history={history}>{children}</Router>
  </Provider>
);

function render(
  ui,
  { route = '/', history = createMemoryHistory({ initialEntries: [route] }), store = makeStore(), ...otherOpts } = {},
) {
  return rtlRender(
    <TestWrapper store={store} history={history}>
      {ui}
    </TestWrapper>,
    otherOpts,
  );
}

export function makeTestStore() {
  const store = makeStore();
  const origDispatch = store.dispatch;
  store.dispatch = jest.fn(origDispatch);
  return store;
}

// re-export everything
export * from '@testing-library/react';
// override render method
export { render };
