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
import Flex from '../components/Flex';

const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const loginError = useSelector((state) => state.errors.loginError);
  const isLoading = useSelector((state) => state.auth.isLoading);
  const dispatch = useDispatch();
  return (
    <Layout>
      <Flex justify="center" align="center">
        <Form
          onSubmit={(e) => {
            e.preventDefault();
            dispatch(loginUser({ username, password }));
          }}
        >
          <Input
            value={username}
            label="Username"
            required={true}
            data-testid="username-field"
            onChange={(event) => setUsername(event.target.value)}
            placeholder="Enter your username"
          />
          <Input
            value={password}
            label="Password"
            required={true}
            type="password"
            data-testid="password-field"
            onChange={(event) => setPassword(event.target.value)}
            placeholder="Enter your password"
          />
          {loginError && (
            <Text type="error" data-testid="error-span">
              {loginError}
            </Text>
          )}
          {isLoading && <Text fontStyle="italic">Loading...</Text>}
          <Button data-testid="login-submit-button" type="submit" className="w-full">
            Login
          </Button>
          <Flex justify="center">
            <Text color="ivory">
              Not registered?{' '}
              <Link to="/register/">
                <Text type="link">Create an account</Text>
              </Link>
            </Text>
          </Flex>
        </Form>
      </Flex>
    </Layout>
  );
};

export default LoginPage;
