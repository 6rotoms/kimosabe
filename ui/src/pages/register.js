import React from 'react';
import { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { registerUser } from '../redux/actions';

import '../styles/register.css';
import { REGISTER_ERROR_MESSAGES } from '../constants';
import Layout from '../components/Layout';

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
      <form
        onSubmit={(e) => {
          e.preventDefault();
          if (fieldsAreValid()) {
            dispatch(registerUser({ username, password }));
          }
        }}
        className="register-form"
      >
        <input
          data-testid="rp-username"
          type="text"
          value={username}
          onChange={(event) => setUsername(event.target.value)}
          placeholder="Username"
          className="input-field register-form-input"
        />
        <span data-testid="rp-username-error" className="error" id="error-username">
          {inlineErrors.username || registerError}
        </span>
        <input
          data-testid="rp-password"
          type="password"
          value={password}
          onChange={(event) => setPassword(event.target.value)}
          placeholder="Password"
          className="input-field register-form-input"
        />
        <span data-testid="rp-password-error" className="error" id="error-password">
          {inlineErrors.password}
        </span>
        <input
          data-testid="rp-cpassword"
          type="password"
          value={cpassword}
          onChange={(event) => setCPassword(event.target.value)}
          placeholder="Confirm Password"
          className="input-field register-form-input"
        />
        <span data-testid="rp-cpassword-error" className="error" id="error-compare">
          {inlineErrors.compare}
        </span>
        {isLoading ? <span className="loading-text">Loading...</span> : null}
        <input
          data-testid="rp-register-button"
          type="submit"
          value="Register"
          className="input-button bgh-red register-form-input"
        />
      </form>
    </Layout>
  );
};

export default RegisterPage;
