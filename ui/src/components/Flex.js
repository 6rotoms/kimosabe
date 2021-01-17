import React from 'react';

const Flex = (props) => {
  const {
    direction = 'row',
    wrap = 'nowrap',
    justify = 'start',
    align = 'stretch',
    children,
    className,
    ...other
  } = props;

  return (
    <div
      {...other}
      className={`flex flex-${direction} flex-${wrap} justify-${justify} items-${align} h-full w-full ${className}`}
    >
      {children}
    </div>
  );
};

export default Flex;
