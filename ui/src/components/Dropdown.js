import React, { useState } from 'react';

const Dropdown = (props) => {
  const { children, visible, content, footer, className, ...other } = props;
  const [active, setActive] = useState(false);

  const handleActivity = () => {
    setActive(true);
  };

  const handleInactivity = (e) => {
    if (!e.currentTarget.contains(e.relatedTarget)) {
      setActive(false);
    }
  };

  return (
    <div onFocus={handleActivity} onBlur={handleInactivity} className="relative max-w-lg" data-test-id="dropdown">
      {children}
      {visible && active && (
        <div {...other} className={`absolute z-10 bg-ivory rounded-lg shadow-md w-full mt-2 ${className}`}>
          {content}
          {footer && <div className="p-2 rounded-b-lg bg-orange text-ivory">{footer}</div>}
        </div>
      )}
    </div>
  );
};

export default Dropdown;
