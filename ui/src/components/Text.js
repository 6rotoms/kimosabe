import React from 'react';

const Text = (props) => {
  const {
    type = 'DEFAULT',
    color = 'ivory',
    decoration,
    fontStyle,
    weight = 'normal',
    size = 'base',
    className = '',
    ...other
  } = props;

  return type === 'link' ? (
    <span
      {...other}
      className={`text-orange ${fontStyle} text-${size} font-${weight} underline hover:text-orange-light ${className}`}
    />
  ) : type === 'error' ? (
    <span {...other} className={`text-red ${fontStyle} text-${size} font-${weight} ${decoration} ${className}`} />
  ) : (
    <span {...other} className={`text-${color} ${fontStyle} text-${size} font-${weight} ${decoration} ${className}`} />
  );
};

export default Text;
