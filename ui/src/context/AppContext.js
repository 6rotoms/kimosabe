import React, { createContext } from 'react';
import { useLocalstorageState } from '../hooks';

const AppContext = createContext();

const defaultValues = {
  loggedIn: false,
};

const AppProvider = ({ initialState = {}, ...props }) => {
  // login state context
  const [loggedIn, setLoggedIn] = useLocalstorageState(
    'kimosabe-login-status',
    initialState.loggedIn || defaultValues.loggedIn,
  );
  return (
    <AppContext.Provider
      value={{
        loggedIn,
        setLoggedIn,
      }}
      {...props}
    />
  );
};

export { AppContext, AppProvider };
