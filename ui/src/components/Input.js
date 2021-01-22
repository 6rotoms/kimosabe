import React from 'react';
import Text from './Text';

const Input = (props) => {
  const { label, required, className, ...other } = props;

  return (
    <div>
      {label && (
        <Text weight="bold">
          {required && <Text color="red">* </Text>}
          {label}
        </Text>
      )}
      <input
        {...other}
        className={`w-full max-w-lg bg-ivory p-2.5 rounded-lg shadow-md focus:outline-none ${className}`}
      />
    </div>
  );
};

export default Input;