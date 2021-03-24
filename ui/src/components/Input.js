import React from 'react';
import Text from './Text';

const Input = ({ label, required, className = '', ...other }) => {
  return (
    <div>
      {label && (
        <Text weight="text-bold">
          {required && <Text color="text-red">* </Text>}
          {label}
        </Text>
      )}
      <input
        {...other}
        className={`w-full max-w-lg bg-ivory p-2.5 rounded-lg focus:shadow-md focus:outline-none ${className}`}
      />
    </div>
  );
};

export default Input;
