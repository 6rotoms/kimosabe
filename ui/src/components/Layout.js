import React from 'react';
import { Header, Grid, Flex } from './index';

const Layout = ({ children, ...other }) => (
  <Grid {...other} rows="layout" className="h-screen">
    <Header siteTitle="kimosabe" />
    <main className="main bg-grey overflow-y-auto">
      <Flex justify="center" align="center">
        <div className="min-h-full h-full w-content mr-auto ml-auto">{children}</div>
      </Flex>
    </main>
  </Grid>
);

export default Layout;
