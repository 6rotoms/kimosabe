import React from 'react';

const Cell = ({ rowSpan = 1, colSpan = 1, rowStart = 1, colStart = 1, className = '', ...other }) => (
  <div
    {...other}
    className={`col-start-${colStart} row-start-${rowStart} col-span-${colSpan} row-span-${rowSpan} ${className}`}
  />
);

export default Cell;
