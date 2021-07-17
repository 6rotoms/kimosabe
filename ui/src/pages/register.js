import React, { useEffect } from 'react';
import { useState } from 'react';
import { useHistory } from 'react-router-dom';
import { REGISTER_ERROR_MESSAGES } from '../constants';
import { Layout, Input, Button, Text, Form, Flex, Tile } from '../components';
import authService from '../services/authService';
import { useAsync } from '../hooks';

const RegisterPage = () => {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [cpassword, setCPassword] = useState('');
  const [inlineErrors, setInlineErrors] = useState('');
  const history = useHistory();
  const { loading, triggerCall, response, error } = useAsync(
    () => authService.register({ username, email, password }),
    { runOnMount: false },
  );

  useEffect(() => {
    if (loading || error) return;
    if (response) {
      history.push('/login');
    }
  }, [loading, response, error]);

  const fieldsAreValid = () => {
    const errors = {};
    if (username.length < 3) {
      errors['username'] = REGISTER_ERROR_MESSAGES.USERNAME_TOO_SHORT;
    }
    if (password.length < 3) {
      errors['password'] = REGISTER_ERROR_MESSAGES.PASSWORD_TOO_SHORT;
    }
    if (password.localeCompare(cpassword) !== 0) {
      errors['compare'] = REGISTER_ERROR_MESSAGES.CONFIRM_PASSWORD_FAILED;
    }
    setInlineErrors(errors);
    return Object.keys(errors).length === 0;
  };

  return (
    <Layout isLoading={loading}>
      <Flex justify="justify-center" align="items-center">
        <Tile className="max-w-xl">
          <Form
            onSubmit={(e) => {
              e.preventDefault();
              if (fieldsAreValid()) {
                triggerCall();
              }
            }}
          >
            <Flex justify="justify-center">
              <Text size="text-3xl">Sign Up</Text>
            </Flex>
            <div>
              <Input
                label="Username"
                required={true}
                data-testid="rp-username"
                type="text"
                value={username}
                onChange={(event) => setUsername(event.target.value)}
                placeholder="Enter your username"
              />
              <Text type="error" data-testid="rp-username-error">
                {inlineErrors.username}
              </Text>
            </div>
            <div>
              <Input
                label="Email"
                required={true}
                data-testid="rp-email"
                type="text"
                value={email}
                onChange={(event) => setEmail(event.target.value)}
                placeholder="Enter your email"
              />
              <Text type="error" data-testid="rp-email-error">
                {inlineErrors.email}
              </Text>
            </div>
            <div>
              <Input
                label="Password"
                required={true}
                data-testid="rp-password"
                type="password"
                value={password}
                onChange={(event) => setPassword(event.target.value)}
                placeholder="Enter your password"
              />
              <Text type="error" data-testid="rp-password-error">
                {inlineErrors.password}
              </Text>
            </div>
            <div>
              <Input
                label="Confirm Password"
                required={true}
                data-testid="rp-cpassword"
                type="password"
                value={cpassword}
                onChange={(event) => setCPassword(event.target.value)}
                placeholder="Re-enter your password"
              />
              <Text type="error" data-testid="rp-cpassword-error">
                {inlineErrors.compare || error}
              </Text>
            </div>
            <Button data-testid="rp-register-button" type="submit" className="w-full">
              <Text>Register</Text>
            </Button>
          </Form>
        </Tile>
      </Flex>
    </Layout>
  );
};

export default RegisterPage;
