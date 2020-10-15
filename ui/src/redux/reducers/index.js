import { combineReducers } from 'redux';
import authReducer from './authReducer';
import errorReducer from './errorReducer';
import searchReducer from './searchReducer';

const rootReducer = combineReducers({
  auth: authReducer,
  errors: errorReducer,
  search: searchReducer,
});

export default rootReducer;
