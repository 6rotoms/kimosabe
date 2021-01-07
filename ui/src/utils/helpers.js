import { useState, useEffect } from 'react';
export const debounce = (callback, wait) => {
  let t;
  return async function (...args) {
    if (t) clearTimeout(t);
    t = setTimeout(() => callback.apply(this, args), wait);
  };
};

export function useDebounce(value, wait) {
  const [debouncedValue, setDebouncedValue] = useState(value);

  useEffect(
    () => {
      // Update debounced value after delay
      const handler = setTimeout(() => {
        setDebouncedValue(value);
      }, wait);

      return () => {
        clearTimeout(handler);
      };
    },
    [value, wait]
  );

  return debouncedValue;
}