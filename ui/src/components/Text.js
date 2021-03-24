import React from 'react';

const Text = (props) => {
  const {
    type = 'DEFAULT',
    color,
    fontStyle = '',
    weight = 'font-normal',
    size = 'text-base',
    className = '',
    ...other
  } = props;
  let finalColor;
  let finalClassName;
  if (type === 'link') {
    finalColor = color ?? 'text-orange';
    finalClassName = 'underline hover:text-orange-light ' + className;
  } else if (type === 'error') {
    finalColor = color ?? 'text-red';
    finalClassName = className;
  } else {
    finalColor = color ?? 'text-ivory';
    finalClassName = className;
  }
  return <span {...other} className={`${finalColor} ${fontStyle} ${size} ${weight} ${finalClassName}`} />;
};

export default Text;
