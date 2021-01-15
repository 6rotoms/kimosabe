import React from 'react';

const Grid = (props) => {
  const { cols = '1', rows = '1', gap = 0, className, ...other } = props;

  return <div {...other} className={`grid grid-cols-${cols} grid-rows-${rows} gap-${gap} ${className}`} />;
};

export default Grid;
