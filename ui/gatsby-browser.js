/**
 * Implement Gatsby's Browser APIs in this file.
 *
 * See: https://www.gatsbyjs.org/docs/browser-apis/
 */

// You can delete this file if you're not using it
import wrapWithProvider from './src/redux/reduxWrapper';
import './src/styles/global.css';

export const wrapRootElement = wrapWithProvider;
