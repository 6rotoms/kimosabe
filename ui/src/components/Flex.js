import React from 'react';

/* Container that will take up all available space, useful for aligning other elements */
const Flex = (props) => {
  const {
    direction = 'row',
    wrap = 'nowrap',
    justify = 'start',
    align = 'stretch',
    className = '',
    height = 'full',
    children,
    ...other
  } = props;

  return (
    <div
      {...other}
      className={
        `flex flex-${direction} ` +
        `flex-${wrap} ` +
        `justify-${justify} ` +
        `h-${height} ` +
        `items-${align} ` +
        `w-full ${className}`
      }
    >
      {children}
    </div>
  );
};

export default Flex;
