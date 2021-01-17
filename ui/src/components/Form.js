import React from 'react';

const Form = (props) => {
  const { className, ...other } = props;

  return <form {...other} className={`grid grid-rows-auto gap-y-8 w-full max-w-lg ${className}`} />;
};

export default Form;
