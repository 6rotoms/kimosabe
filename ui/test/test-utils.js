// test-utils.js
import React from 'react';
import { render as rtlRender } from '@testing-library/react';
import { Provider } from 'react-redux';
import { makeStore } from '../src/redux/store';

const TestProvider = ({
  store,
  children,
}) => <Provider store={store}>{children}</Provider>;

function render(ui, { store, ...otherOpts }) {
  return rtlRender(<TestProvider store={store}>{ui}</TestProvider>, otherOpts);
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
