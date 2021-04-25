import React from 'react';
import PropTypes from 'prop-types';
import { Header, Grid, Flex, LoadingWheel } from './index';

const Layout = ({ isLoading, children, ...other }) => (
  <Grid {...other} rows="grid-rows-layout" cols="grid-cols-1" className="h-screen">
    <Header siteTitle="kimosabe" />
    <main className="overflow-y-auto main bg-grey">
      <Flex justify="justify-center" align="items-center">
        <div className="h-full min-h-full ml-auto mr-auto w-content">
          {
            isLoading ?
            <Flex justify="justify-center" align="items-center">
              <LoadingWheel/>
            </Flex> :
            children
          }
        </div>
      </Flex>
    </main>
  </Grid>
);

Layout.propTypes = {
  isLoading: PropTypes.bool,
};

Layout.defaultProps = {
  isLoading: false,
};

export default Layout;
