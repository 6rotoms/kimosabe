import React from 'react';
import { Route, Switch, useLocation } from 'react-router-dom';
import { HomePage, LoginPage, RegisterPage, SearchPage, UserProfilePage } from './pages';

import './styles/global.css';

function useQuery() {
  return new URLSearchParams(useLocation().search);
}

function App() {
  const query = useQuery();

  return (
    <Switch>
      <Route exact path="/login">
        <LoginPage />
      </Route>
      <Route exact path="/register">
        <RegisterPage />
      </Route>
      <Route exact path="/user/:username">
        <UserProfilePage />
      </Route>
      <Route exact path="/">
        <HomePage />
      </Route>
      <Route path="/search">
        <SearchPage searchTerm={query.get('term')} pageNum={query.get('page')} />
      </Route>
    </Switch>
  );
}

export default App;
