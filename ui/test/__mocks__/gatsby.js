const React = require('react');
const gatsby = jest.requireActual('gatsby');

const graphql = require('./graphql');

module.exports = {
  ...gatsby,
  navigate: jest.fn(),
  graphql: jest.fn(),
  Link: jest.fn().mockImplementation(
    // these props are invalid for an `a` tag
    ({
      to,
      ...rest
    }) =>
      React.createElement('a', {
        ...rest,
        href: to,
      }),
  ),
  StaticQuery: jest.fn(),
  useStaticQuery: jest
    .fn()
    .mockImplementation(graphql.layoutQuery)
    .mockImplementation(graphql.SEOQuery),
};
