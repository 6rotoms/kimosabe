import { useState } from 'react';
/**
 * hook for setting and retrieving a value from local storage. Usage similar to useState.
 * @param {*} key is the local storage key that the information is stored under
 * @param {*} initialValue is the initial value that should be used if the key is not populated
 */
function useLocalstorageState(key, initialValue) {
  const [storedValue, setStoredValue] = useState(() => {
    try {
      const item = window.localStorage.getItem(key);
      return item ? JSON.parse(item) : initialValue;
    } catch (error) {
      console.error(error);
      return initialValue;
    }
  });

  const setValue = (value) => {
    try {
      const valueToStore = value instanceof Function ? value(storedValue) : value;
      setStoredValue(valueToStore);
      window.localStorage.setItem(key, JSON.stringify(valueToStore));
    } catch (error) {
      console.error(error);
    }
  };

  return [storedValue, setValue];
}

export default useLocalstorageState;
