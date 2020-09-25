// test-utils.js
import React from 'react';
import { render as rtlRender } from '@testing-library/react';
import { Provider } from 'react-redux';
import { BrowserRouter } from 'react-router-dom';
import { makeStore } from '../redux/store';

const TestWrapper = ({ store, children }) => (
  <Provider store={store}>
    <BrowserRouter>{children}</BrowserRouter>
  </Provider>
);

function render(ui, { store, ...otherOpts }) {
  return rtlRender(<TestWrapper store={store}>{ui}</TestWrapper>, otherOpts);
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
