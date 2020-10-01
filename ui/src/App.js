import React from 'react';
import { Route, Switch, useLocation } from 'react-router-dom';
import Header from './components/header';
import { HomePage, LoginPage, RegisterPage, SearchPage } from './pages';
import './styles/global.css';


function useQuery() {
  return new URLSearchParams(useLocation().search);
}

function App() {
  const query = useQuery();

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
          <Route path="/search">
            <SearchPage searchTerm={query.get('term')} pageNum={query.get('page')}/>
          </Route>
        </Switch>
      </main>
    </div>
  );
}

export default App;
