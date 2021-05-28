import React from 'react';
import { useSelector } from 'react-redux';
import { Layout, Text } from '../components';

const HomePage = () => {
  const loggedIn = useSelector((state) => state.auth.loggedIn);
  return (
    <Layout>
      <Text>{loggedIn ? 'Welcome!' : 'Kimosabe placeholder'}</Text>
    </Layout>
  );
};

export default HomePage;
