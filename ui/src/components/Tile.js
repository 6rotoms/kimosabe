import React from 'react';
import PropTypes from 'prop-types';
import Text from './Text';

const Tile = ({
  title,
  width = 'full',
  height = 'auto',
  children,
  pd = '8',
  className = '',
  titleAlign = 'center',
}) => {
  return (
    <div className={`w-${width} h-${height} bg-grey-dark rounded-lg shadow-lg ${className}`}>
      <div className={`w-${width} h-${height} p-${pd}`}>
        {title && (
          <div data-testid="title-div">
            <Text alignment={titleAlign}>{title}</Text>
            <div className="my-px10 bg-grey w-full h-px" />
          </div>
        )}
        {children}
      </div>
    </div>
  );
};

Tile.propTypes = {
  // Pass in element or string.
  title: PropTypes.oneOfType([PropTypes.string, PropTypes.element]),
  width: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
  height: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
  pd: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
  className: PropTypes.string,
  children: PropTypes.element,
  // Alignment of title: Either left, right, or center.
  titleAlign: PropTypes.string,
};

export default Tile;
