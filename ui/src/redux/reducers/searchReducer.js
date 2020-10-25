import { SEARCH_REQUEST } from '../../constants';

export const initialState = {
  searchTerm: '',
};

const searchReducer = (state = initialState, action) => {
  switch (action.type) {
    case SEARCH_REQUEST: {
      return {
        ...state,
        searchTerm: action.searchTerm,
      };
    }
    default:
      return state;
  }
};

export default searchReducer;
