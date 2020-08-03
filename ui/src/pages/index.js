import React from 'react';
import { Link } from 'gatsby';

import Layout from '../components/layout';
import SEO from '../components/seo';

const IndexPage = () => (
  <Layout>
    <SEO title="Home" />
    <h1>Header</h1>
    <p>Kimosabe placeholder.</p>
    <Link to="/login/">Temp Login Link</Link> <br />
  </Layout>
);

export default IndexPage;
