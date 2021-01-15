import React from 'react';
import { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link } from 'react-router-dom';
import { loginUser } from '../redux/actions';
import Layout from '../components/Layout';
import Input from '../components/Input';
import Button from '../components/Button';
import Text from '../components/Text';
import Form from '../components/Form';

const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const loginError = useSelector((state) => state.errors.loginError);
  const isLoading = useSelector((state) => state.auth.isLoading);
  const dispatch = useDispatch();
  return (
    <Layout>
      <Form
        onSubmit={(e) => {
          e.preventDefault();
          dispatch(loginUser({ username, password }));
        }}
        className="flex flex-row justify-center items-center"
      >
        <Input
          value={username}
          data-testid="username-field"
          onChange={(event) => setUsername(event.target.value)}
          placeholder="Username"
        />
        <Input
          value={password}
          type="password"
          data-testid="password-field"
          onChange={(event) => setPassword(event.target.value)}
          placeholder="Password"
        />
        {loginError && (
          <Text type="error" data-testid="error-span">
            {loginError}
          </Text>
        )}
        {isLoading && <Text fontStyle="italic">Loading...</Text>}
        <Button data-testid="login-submit-button" type="submit">
          Login
        </Button>
        <Text color="ivory">
          Not registered?{' '}
          <Link to="/register/">
            <Text type="link">Create an account</Text>
          </Link>
        </Text>
      </Form>
    </Layout>
  );
};

export default LoginPage;
