import React from 'react';
import { Switch, Route } from 'react-router-dom';
import { LoginPage, RegisterPage, HomePage, SearchPage } from './pages';
import Header from './components/header';

import './styles/global.css';

function App() {
  return (
    <div className="layout">
      <Header siteTitle="kimosabe" />
      <main className="main">
        <Switch>
          <Route exact path="/login">
            <LoginPage />
          </Route>
          <Route exact path="/register">
            <RegisterPage />
          </Route>
          <Route exact path="/">
            <HomePage />
          </Route>
          <Route exact path="/search">
            <SearchPage />
          </Route>
        </Switch>
      </main>
    </div>
  );
}

export default App;
