import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';
import React, { useState } from 'react';
import history from '../history';
import '../styles/header.css';

const Header = ({ siteTitle }) => {
  const [searchTerm, setSearchTerm] = useState('');

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
        <Link to="/login/" className="input-button bgh-red header-login__button">
          Login
        </Link>
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
