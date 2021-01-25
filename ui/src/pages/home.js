import React from 'react';
import { useSelector } from 'react-redux';
import { Layout } from '../components';

const HomePage = () => {
  const loggedIn = useSelector((state) => state.auth.loggedIn);
  return (
    <Layout>
      <p>{loggedIn ? 'Welcome!' : 'Kimosabe placeholder'}</p>
    </Layout>
  );
};

export default HomePage;
