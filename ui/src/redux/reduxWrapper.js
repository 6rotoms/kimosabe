import React from 'react';
import { Provider } from 'react-redux';
import { makeStore } from './store';
const store = makeStore();
const reduxWrapper = ({ element }) => (
  <Provider store={store}>{element}</Provider>
);

export default reduxWrapper;
