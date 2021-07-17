import React from 'react';
import { Layout, Text } from '../components';
import { useLogin } from '../hooks';

const HomePage = () => {
  const [loggedIn] = useLogin();
  return (
    <Layout>
      <Text>{loggedIn ? 'Welcome!' : 'Kimosabe placeholder'}</Text>
    </Layout>
  );
};

export default HomePage;
