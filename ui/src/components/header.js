import { Link } from 'gatsby';
import PropTypes from 'prop-types';
import React from 'react';
import '../styles/header.css';

import { useDispatch } from 'react-redux';
import { searchUpdate } from '../redux/actions';

const Header = ({ siteTitle }) => {
  const dispatch = useDispatch();

  return (
    <header className="header-container">
      <div className="header-title">
        <Link to="/" className="header-title__text">
          {siteTitle}
        </Link>
        <input
          onChange={e => {
            e.preventDefault();
            dispatch(searchUpdate({ searchTerm: e.target.value }));
          }}
          type="text"
          placeholder="Look For Group..."
          className="search-field header-title__search"
        />
      </div>
      <div className="header-login">
        <Link to="/login/" className="input-button header-login__button">
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
