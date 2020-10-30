import React from 'react';
import PropTypes from 'prop-types';
import '../styles/tile.css';

const Tile = ({ title, children, className = '', titleAlign = 'center' }) => {
  const alignment = { textAlign: titleAlign };
  return (
    <div className={`tile-container ${className}`}>
      {title && (
        <div data-testid="title-div">
          <div data-testid="title-content" className="title" style={alignment}>
            {title}
          </div>
          <div className="underline" />
        </div>
      )}
      {children}
    </div>
  );
};

Tile.propTypes = {
  // Pass in element or string.
  title: PropTypes.oneOfType([PropTypes.string, PropTypes.element]),
  children: PropTypes.element,
  // Alignment of title: Either left, right, or center.
  titleAlign: PropTypes.string,
};

export default Tile;
