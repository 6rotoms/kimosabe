import React, { useState } from 'react';

const Dropdown = (props) => {
  const { children, visible, content, footer, className, ...other } = props;
  const [active, setActive] = useState(false);

  const handleActivity = () => {
    setActive(true);
  };

  const handleInactivity = () => {
    setActive(false);
  };

  return (
    <div onFocus={handleActivity} onBlur={handleInactivity} className="relative">
      {children}
      {visible && active && (
        <div {...other} className={`absolute z-10 bg-ivory rounded-lg shadow-md w-full mt-2 ${className}`}>
          {content}
          {footer && <div className="bg-orange text-ivory rounded-b-lg p-2">{footer}</div>}
        </div>
      )}
    </div>
  );
};

export default Dropdown;
