import React from 'react';
import { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link } from 'react-router-dom';
import { loginUser } from '../redux/actions';
import { Layout, Input, Button, Text, Form, Flex, Tile } from '../components';

const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const loginError = useSelector((state) => state.errors.loginError);
  const isLoading = useSelector((state) => state.auth.isLoading);
  const dispatch = useDispatch();
  return (
    <Layout>
      <Flex justify="justify-center" align="items-center">
        <Tile className="max-w-xl">
          <Form
            onSubmit={(e) => {
              e.preventDefault();
              dispatch(loginUser({ username, password }));
            }}
          >
            <Flex justify="justify-center">
              <Text size="text-3xl">Sign In</Text>
            </Flex>
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
            <Flex justify="justify-center">
              <Text color="text-ivory">
                Not registered?{' '}
                <Link to="/register/">
                  <Text type="link">Create an account</Text>
                </Link>
              </Text>
            </Flex>
            {isLoading && <Text fontStyle="text-italic">Loading...</Text>}
            <Button data-testid="login-submit-button" type="submit" className="w-full">
              Login
            </Button>
          </Form>
        </Tile>
      </Flex>
    </Layout>
  );
};

export default LoginPage;
