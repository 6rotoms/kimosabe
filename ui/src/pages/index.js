import React from 'react';
import { Link } from 'gatsby';
import { useSelector } from 'react-redux';

import Layout from '../components/layout';
import SEO from '../components/seo';

const IndexPage = () => {

  const loggedIn = useSelector((state) => state.auth.loggedIn);
  return (
    <Layout>
      <SEO title="Home" />
      <h1>Header</h1>
      <p>{loggedIn ? 'Welcome!' : 'Kimosabe placeholder'}</p>
      <Link to="/login/">Temp Login Link</Link> <br />
      <Link to="/register/">Temp Register Link</Link> <br />
    </Layout>
  );
};

export default IndexPage;
