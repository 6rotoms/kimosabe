import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';
import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import history from '../history';
import '../styles/header.css';
import { logoutUser } from '../redux/actions';

const Header = ({ siteTitle }) => {
  const [searchTerm, setSearchTerm] = useState('');
  const dispatch = useDispatch();
  const isLoggedIn = useSelector((state) => state.auth.loggedIn);
  return (
    <header className="header-container">
      <div className="header-title">
        <Link to="/" className="header-title__text">
          {siteTitle}
        </Link>
        <input
          data-testid="header-search"
          onChange={(e) => {
            e.preventDefault();
            setSearchTerm(e.target.value);
          }}
          onKeyUp={(e) => {
            if (e.keyCode === 13) {
              e.preventDefault();
              history.push(`/search?term=${encodeURI(searchTerm)}&page=0`);
            }
          }}
          type="text"
          placeholder="Look For Group..."
          className="search-field header-title__search"
        />
      </div>
      <div className="header-login">
        {isLoggedIn ? (
          <button className="input-button bgh-red" onClick={() => dispatch(logoutUser())}>
            Logout
          </button>
        ) : (
          <Link to="/login/" className="input-button bgh-red header-login__button">
            Login
          </Link>
        )}
      </div>
    </header>
  );
};

Header.propTypes = {
  siteTitle: PropTypes.string,
};

Header.defaultProps = {
  siteTitle: '',
};

export default Header;
