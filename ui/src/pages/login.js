import React from 'react';
import { useState, useEffect } from 'react';
import { Link, useHistory } from 'react-router-dom';
import { Layout, Input, Button, Text, Form, Flex, Tile } from '../components';
import { useLocalstorageState, useAsync } from '../hooks';
import authService from '../services/authService';

const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [, setLoggedIn] = useLocalstorageState('isLoggedIn', false);
  const { loading, triggerCall, response, error } = useAsync(() => authService.login({ username, password }), {
    runOnMount: false,
  });
  const history = useHistory();

  useEffect(() => {
    if (loading || error) return;
    if (response) {
      setLoggedIn(true);
      history.push('/');
    }
  }, [loading, response, error]);

  return (
    <Layout isLoading={loading}>
      <Flex justify="justify-center" align="items-center">
        <Tile className="max-w-xl">
          <Form
            onSubmit={(e) => {
              e.preventDefault();
              triggerCall();
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
            {error && (
              <Text type="error" data-testid="error-span">
                {error}
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
            {loading && <Text className="text-italic">Loading...</Text>}
            <Button data-testid="login-submit-button" type="submit" className="w-full">
              <Text>Login</Text>
            </Button>
          </Form>
        </Tile>
      </Flex>
    </Layout>
  );
};

export default LoginPage;
