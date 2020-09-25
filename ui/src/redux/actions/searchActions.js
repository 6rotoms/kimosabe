import 'cross-fetch/polyfill';

import { SEARCH_REQUEST } from '..//../constants/actions.js';

export const searchUpdate = ({ searchTerm }) => {
    return ({
        type: SEARCH_REQUEST,
        searchTerm: searchTerm,
    });
};