import React from 'react';
import { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { registerUser } from '../redux/actions';


import '../styles/global.css';

const RegisterPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [cpassword, setCPassword] = useState('');
    const registerError = useSelector((state) => state.errors.registerError);
    const isLoading = useSelector((state) => state.auth.isLoading);
    const dispatch = useDispatch();

    const fieldValidation = () => {
      document.getElementById('error-username').innerText = '';
      document.getElementById('error-password').innerText = '';
      document.getElementById('error-compare').innerText = '';
      let errored = false;
      if (username.length < 3){
        document.getElementById('error-username').innerText = 'Username should be at least 3 characters';
        errored = true;
      }
      if (password.length < 3){
        document.getElementById('error-password').innerText = 'Password should be at least 3 characters';
        errored = true;
      }
      if (password.localeCompare(cpassword) !== 0){
        document.getElementById('error-compare').innerText = 'Passwords should match';
        errored = true;
      }
      return !errored;
    };

    return(<div className='main-content'>
      <form onSubmit={(e) => {
        e.preventDefault();
        if(fieldValidation()){
          dispatch(registerUser({ username, password }));
        }
        }} className='register-form'>
        <input type='text' value={username} onChange={(event) => setUsername(event.target.value)} placeholder='Username' className='input-field'/>
        <span className='error' id='error-username'></span>
        <span data-testid="error-span" className='error'>{registerError}</span>
        <input type='password' value={password} onChange={(event) => setPassword(event.target.value)} placeholder='Password' className='input-field'/>
        <span className='error' id='error-password'></span>
        <input type='password' value={cpassword} onChange={(event) => setCPassword(event.target.value)} placeholder='Confirm Password' className='input-field'/>
        <span className='error' id='error-compare'></span>
        {isLoading ? <span className='loading-text'>Loading...</span> : null }
        <input type="submit" value="Register" className='input-button'/>
      </form>
    </div>);
};

export default RegisterPage;