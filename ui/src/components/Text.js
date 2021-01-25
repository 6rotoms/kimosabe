import React from 'react';

const Text = (props) => {
  const {
    type = 'DEFAULT',
    color,
    fontStyle,
    weight = 'normal',
    size = 'base',
    className = '',
    alignment = 'left',
    ...other
  } = props;
  let finalColor;
  let finalClassName;
  if (type === 'link') {
    if (!color) finalColor = 'orange';
    finalColor = color ? color : 'orange';
    finalClassName = 'underline hover:text-orange-light ' + className;
  } else if (type === 'error') {
    finalColor = color ? color : 'red';
    finalClassName = className;
  } else {
    finalColor = color ? color : 'ivory';
    finalClassName = className;
  }
  return (
    <span
      {...other}
      className={`text-${finalColor} ${fontStyle} text-${size} text-${alignment} font-${weight} ${finalClassName}`}
    />
  );
};

export default Text;
