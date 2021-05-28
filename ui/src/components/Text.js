import React from 'react';
import PropTypes from 'prop-types';

const Text = ({ type, color, weight, size, className, ...other }) => {
  let finalColor;
  let finalClassName = className;
  if (type === 'link') {
    finalColor = color ?? 'text-orange';
    finalClassName = 'underline hover:text-orange-light ' + className;
  } else if (type == 'success') {
    finalColor = color ?? 'text-green';
  } else if (type === 'error') {
    finalColor = color ?? 'text-red';
  } else {
    finalColor = color ?? 'text-ivory';
  }
  return <span {...other} className={`${finalColor} ${size} ${weight} ${finalClassName}`} />;
};

Text.propTypes = {
  type: PropTypes.string,
  color: PropTypes.string,
  weight: PropTypes.string,
  size: PropTypes.string,
  className: PropTypes.string,
};

Text.defaultProps = {
  type: 'DEFAULT',
  weight: 'font-medium',
  size: 'text-base',
  className: '',
};

export default Text;
