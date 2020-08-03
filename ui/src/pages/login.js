import React from 'react';
import { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import Layout from '../components/layout';
import SEO from '../components/seo';
import { loginUser } from '../redux/actions';

const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const loginError = useSelector((state) => state.errors.loginError);
  const isLoading = useSelector((state) => state.auth.isLoading);
  const dispatch = useDispatch();

  return (
    <Layout>
      <SEO title="Home" />
      <form
        onSubmit={(e) => {
          e.preventDefault();
          dispatch(loginUser({ username, password }));
        }}
      >
        <input
          value={username}
          data-testid="username-field"
          onChange={(event) => setUsername(event.target.value)}
        />
        <input
          value={password}
          data-testid="password-field"
          onChange={(event) => setPassword(event.target.value)}
        />
        <input type="submit" value="Login" />
        <span>{loginError}</span>
        {isLoading ? <span>Loading...</span> : null }
      </form>
    </Layout>
  );
};

export default LoginPage;
