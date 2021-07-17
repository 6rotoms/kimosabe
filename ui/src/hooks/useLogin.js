import { useContext } from 'react';
import { AppContext } from '../context/AppContext';
/**
 * hook for getting login status of user
 */
export default function useLogin() {
  const context = useContext(AppContext);
  if (!context) {
    throw new Error('Missing AppContext!');
  }
  const { loggedIn, setLoggedIn } = context;
  return [loggedIn, setLoggedIn];
}
