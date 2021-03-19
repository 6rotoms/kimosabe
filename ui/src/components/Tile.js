import React from 'react';
import PropTypes from 'prop-types';
import Text from './Text';

const Tile = ({
  title = '',
  width = 'w-full',
  height = 'h-auto',
  children,
  pd = 'p-6',
  className = '',
  titleAlign = 'center',
}) => {
  return (
    <div className={`${width} ${height} bg-grey-dark rounded-lg shadow-lg ${className}`}>
      <div className={`w-full h-full ${pd}`}>
        {title && (
          <div data-testid="title-div" className={`${titleAlign} border-b-2 pb-px5 mb-px10 border-ivory-dark`}>
            <Text>{title}</Text>
          </div>
        )}
        {children}
      </div>
    </div>
  );
};

Tile.propTypes = {
  // Pass in element or string.
  title: PropTypes.string,
  width: PropTypes.string,
  height: PropTypes.string,
  pd: PropTypes.string,
  className: PropTypes.string,
  children: PropTypes.element,
  // Alignment of title: Either left, right, or center.
  titleAlign: PropTypes.string,
};

export default Tile;
