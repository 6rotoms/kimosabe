import { createStore, applyMiddleware } from 'redux';
import thunk from 'redux-thunk';

import rootReducer from './reducers';
export const makeStore = () => createStore(rootReducer, applyMiddleware(thunk));
