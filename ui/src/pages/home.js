import React from 'react';
import { Layout, Text } from '../components';
import { useLocalstorageState } from '../hooks';

const HomePage = () => {
  const [loggedIn] = useLocalstorageState('isLoggedIn', false);
  return (
    <Layout>
      <Text>{loggedIn ? 'Welcome!' : 'Kimosabe placeholder'}</Text>
    </Layout>
  );
};

export default HomePage;
