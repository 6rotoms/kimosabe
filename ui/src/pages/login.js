import React from 'react';
import { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link } from 'gatsby';

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
        className='register-form'
      >
        <input
          value={username}
          data-testid="username-field"
          onChange={(event) => setUsername(event.target.value)}
          placeholder='Username'
          className='input-field register-form-input'
        />
        <input
          value={password}
          data-testid="password-field"
          onChange={(event) => setPassword(event.target.value)}
          placeholder='Password'
          className='input-field register-form-input'
        />
        <span data-testid='error-span' className='error'>{loginError}</span>
        {isLoading ? <span>Loading...</span> : null }
        <input
          data-testid="login-submit-button"
          type="submit"
          value="Login"
          className='input-button register-form-input'
        />
        <p>Not registered? <Link to="/register/" className='link'>Create an account</Link></p>
      </form>
    </Layout>
  );
};

export default LoginPage;
