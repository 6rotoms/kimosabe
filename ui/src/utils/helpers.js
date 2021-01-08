import { useState, useEffect } from 'react';

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