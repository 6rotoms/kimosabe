import React from 'react';
import { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { registerUser } from '../redux/actions';

import { REGISTER_ERROR_MESSAGES } from '../constants';
import { Layout, Input, Button, Text, Form, Flex, Tile } from '../components';

const RegisterPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [cpassword, setCPassword] = useState('');
  const [inlineErrors, setInlineErrors] = useState({ username: '', password: '', compare: '' });
  const registerError = useSelector((state) => state.errors.registerError);
  const isLoading = useSelector((state) => state.auth.isLoading);
  const dispatch = useDispatch();

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
    <Layout>
      <Flex justify="center" align="center">
        <Tile className="max-w-xl">
          <Form
            onSubmit={(e) => {
              e.preventDefault();
              if (fieldsAreValid()) {
                dispatch(registerUser({ username, password }));
              }
            }}
          >
            <Flex justify="center">
              <Text size="3xl">Sign Up</Text>
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
              <Text type="error" data-testid="rp-username-error" id="error-username">
                {inlineErrors.username || registerError}
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
              <Text type="error" data-testid="rp-password-error" id="error-password">
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
              <Text type="error" data-testid="rp-cpassword-error" id="error-compare">
                {inlineErrors.compare}
              </Text>
            </div>
            {isLoading ? <Text>Loading...</Text> : null}
            <Button data-testid="rp-register-button" type="submit" className="w-full">
              Register
            </Button>
          </Form>
        </Tile>
      </Flex>
    </Layout>
  );
};

export default RegisterPage;
