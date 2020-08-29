import React from 'react';
import { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { registerUser } from '../redux/actions';

import '../styles/register.css';

const RegisterPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [cpassword, setCPassword] = useState('');
    const [inlineErrors, setInlineErrors] = useState({username: '', password: '', compare: ''});
    const registerError = useSelector((state) => state.errors.registerError);
    const isLoading = useSelector((state) => state.auth.isLoading);
    const dispatch = useDispatch();

    const fieldsAreValid = () => {
      const errors = {};
      if (username.length < 3){
        errors['username'] = 'Username should be at least 3 characters';
      }
      if (password.length < 3){
        errors['password'] = 'Password should be at least 3 characters';
      }
      if (password.localeCompare(cpassword) !== 0){
        errors['compare'] = 'Passwords should match';
      }
      setInlineErrors(errors);
      return Object.keys(errors).length === 0;
    };

    return(<div className='main-content'>
      <form onSubmit={(e) => {
        e.preventDefault();
        if(fieldsAreValid()){
          dispatch(registerUser({ username, password }));
        }
        }} className='register-form'>
        <input type='text' value={username} onChange={(event) => setUsername(event.target.value)} placeholder='Username' className='input-field register-form-input'/>
        <span className='error' id='error-username'>{inlineErrors.username || registerError }</span>
        <input type='password' value={password} onChange={(event) => setPassword(event.target.value)} placeholder='Password' className='input-field register-form-input'/>
        <span className='error' id='error-password'>{inlineErrors.password}</span>
        <input type='password' value={cpassword} onChange={(event) => setCPassword(event.target.value)} placeholder='Confirm Password' className='input-field register-form-input'/>
        <span className='error' id='error-compare'>{inlineErrors.compare}</span>
        {isLoading ? <span className='loading-text'>Loading...</span> : null }
        <input type="submit" value="Register" className='input-button register-form-input'/>
      </form>
    </div>);
};

export default RegisterPage;