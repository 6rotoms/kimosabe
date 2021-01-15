import React from 'react';

const Input = (props) => {
  const { className, ...other } = props;

  return (
    <input {...other} className={`max-w-lg bg-ivory p-2.5 rounded-lg shadow-md focus:outline-none ${className}`} />
  );
};

export default Input;
