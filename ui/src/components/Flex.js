import React from 'react';

/* Container that will take up all available space, useful for aligning other elements */
const Flex = (props) => {
  const {
    direction = 'flex-row',
    wrap = false,
    justify = 'justify-start',
    align = 'items-stretch',
    className = '',
    height = 'h-full',
    children,
    ...other
  } = props;

  return (
    <div
      {...other}
      className={`flex w-full ${direction} ${
        wrap ? 'flex-wrap' : 'flex-nowrap'
      } ${justify} ${height} ${align} ${className}`}
    >
      {children}
    </div>
  );
};

export default Flex;
