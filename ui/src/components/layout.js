/**
 * Layout component that queries for data
 * with Gatsby's useStaticQuery component
 *
 * See: https://www.gatsbyjs.org/docs/use-static-query/
 */

import React from 'react';

import Header from './header';

const Layout = ({ children }) => {
  return (
    <div className="layout">
      <Header siteTitle="kimosabe" />
      <main className="main">{children}</main>
    </div>
  );
};

export default Layout;
