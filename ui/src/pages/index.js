import React from 'react';
//import { useSelector } from 'react-redux';

import Layout from '../components/layout';
import SEO from '../components/seo';

const IndexPage = () => {
  //const loggedIn = useSelector((state) => state.auth.loggedIn);
  return (
    <Layout>
      <SEO title="Home" />
    </Layout>
  );
};

export default IndexPage;
