import React from 'react';
import Header from './Header';

const Layout = ({ children, ...other }) => (
  <div {...other} className="layout">
    <Header siteTitle="kimosabe" />
    <main className="main">
        {children}
    </main>
  </div>
);

export default Layout;
