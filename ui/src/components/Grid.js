import React from 'react';

const Grid = ({
  cols = 'grid-cols-0',
  rows = 'grid-rows-0',
  gap = 'gap-0',
  justify = 'justify-normal',
  className = '',
  ...other
}) => {
  return <div {...other} className={`grid ${rows} ${cols} ${justify} ${gap} ${className}`} />;
};

export default Grid;
