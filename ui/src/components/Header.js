import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';
import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import history from '../history';
import '../styles/header.css';
import { logoutUser } from '../redux/actions';
import gameService from '../services/gameService';
import DropdownSearch from './DropdownSearch';

const Header = ({ siteTitle }) => {
  const dispatch = useDispatch();
  const isLoggedIn = useSelector((state) => state.auth.loggedIn);
  const suggestionsCallback = async (searchTerm) => {
    const response = await gameService.getSuggestions({ searchTerm });
    if (response.status !== 200) {
      return [];
    }
    const newSuggestions = response.body;
    return newSuggestions.map(suggestion => ({
      id: suggestion.id,
      link: `/group/${suggestion.id}`,
      text: suggestion.name,
      imgUrl: suggestion.thumbUrl,
    }));
  };
  return (
    <header className="header-container">
      <div className="header-title">
        <Link to="/" className="header-title__text">
          {siteTitle}
        </Link>
      </div>
      <DropdownSearch
        suggestionsCallback={suggestionsCallback}
        onSearch={(searchTerm) => history.push(`/search?term=${encodeURI(searchTerm)}&page=0`)}
        debounceTime={500}
        showImage
        data-testid="header-search"
        placeholder="Look For Group..."
        className="search-field header-title__search"
      />
      <div className="header-login">
        {isLoggedIn ? (
          <button className="input-button bgh-red" onClick={() => dispatch(logoutUser())}>
            Logout
          </button>
        ) : (
          <Link to="/login/" className="input-button bgh-red">
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
