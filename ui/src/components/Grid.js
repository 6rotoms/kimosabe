import React from 'react';

const Grid = (props) => {
  const { cols = '0', rows = '0', gap = 0, className = '', justify = 'normal', ...other } = props;

  return (
    <div
      {...other}
      className={`grid grid-cols-${cols} justify-${justify} grid-rows-${rows} gap-${gap} ${className}`}
    />
  );
};

export default Grid;
