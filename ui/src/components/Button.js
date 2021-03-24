import React from 'react';
import PropTypes from 'prop-types';

const Button = (props) => {
  const { color = 'red', className = '', ...other } = props;
  const types = {
    red: 'bg-red hover:bg-red-light',
    green: 'bg-green hover:bg-green-light',
    orange: 'bg-orange hover:bg-orange-light',
  };

  return (
    <button
      {...other}
      className={`w-32 ${types[color]} text-ivory 
      p-2.5 rounded-lg shadow-md focus:outline-none ${className}`}
    />
  );
};

Button.propTypes = {
  color: PropTypes.oneOf(['red', 'green', 'orange']),
  className: PropTypes.string,
};

export default Button;
