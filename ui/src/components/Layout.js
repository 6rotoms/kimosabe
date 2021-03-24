import React from 'react';
import { Header, Grid, Flex } from './index';

const Layout = ({ children, ...other }) => (
  <Grid {...other} rows="grid-rows-layout" cols="grid-cols-1" className="h-screen">
    <Header siteTitle="kimosabe" />
    <main className="overflow-y-auto main bg-grey">
      <Flex justify="justify-center" align="items-center">
        <div className="h-full min-h-full ml-auto mr-auto w-content">{children}</div>
      </Flex>
    </main>
  </Grid>
);

export default Layout;
