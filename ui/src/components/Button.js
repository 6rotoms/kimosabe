import React from 'react';

const Button = (props) => {
  const { color = 'red', className = '', ...other } = props;

  return (
    <button
      {...other}
      className={`w-32 bg-${color} hover:bg-${color}-light text-ivory 
      p-2.5 rounded-lg shadow-md focus:outline-none ${className}`}
    />
  );
};

export default Button;
