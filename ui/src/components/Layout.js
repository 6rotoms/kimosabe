import React from 'react';
import Header from './Header';
import Grid from './Grid';

const Layout = ({ children, ...other }) => (
  <Grid {...other} rows="layout" className="h-screen">
    <Header siteTitle="kimosabe" />
    <main className="main bg-grey">{children}</main>
  </Grid>
);

export default Layout;
