import React from 'react';
import { useSelector } from 'react-redux';

const HomePage = () => {
  const loggedIn = useSelector((state) => state.auth.loggedIn);
  return <p>{loggedIn ? 'Welcome!' : 'Kimosabe placeholder'}</p>;
};

export default HomePage;
