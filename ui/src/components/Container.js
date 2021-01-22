import React from 'react';

/* TODO: Fix problem with container shrinking elements, temporary fix sets a max width of 36rem */
const Container = (props) => {
  const { children, className, ...other } = props;

  return (
    <div {...other} className={`w-full p-8 bg-grey-dark rounded-lg shadow-lg ${className}`}>
      {children}
    </div>
  );
};

export default Container;
