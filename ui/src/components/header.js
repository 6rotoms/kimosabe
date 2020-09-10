import { Link } from 'gatsby';
import PropTypes from 'prop-types';
import React from 'react';
import '../styles/header.css';

const Header = ({ siteTitle }) => (
    <header className='header-container'>
      <div className='header-title'>
        <Link to="/" className='header-title__text'>{siteTitle}</Link>
        <input type='text' placeholder='Look For Group...' className='search-field header-title__search'/>
      </div>
      <div className='header-login'>
        <Link to="/login/" className='input-button header-login__button'>Login</Link>
      </div>
    </header>
);

Header.propTypes = {
  siteTitle: PropTypes.string,
};

Header.defaultProps = {
  siteTitle: '',
};

export default Header;
