import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';
import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import history from '../history';
import { logoutUser } from '../redux/actions';
import gameService from '../services/gameService';
import { Grid, DropdownSearch, Button, Text } from './index';

const Header = ({ siteTitle }) => {
  const dispatch = useDispatch();
  const isLoggedIn = useSelector((state) => state.auth.loggedIn);
  const suggestionsCallback = async (searchTerm) => {
    const response = await gameService.getSuggestions({ searchTerm });
    if (response.status !== 200) {
      return [];
    }
    const newSuggestions = response.body;
    return newSuggestions.map((suggestion) => ({
      id: suggestion.id,
      link: `/group/${suggestion.id}`,
      text: suggestion.name,
      imgUrl: suggestion.thumbUrl,
    }));
  };
  return (
    <Grid cols="header" gap={4} className="bg-orange p-4">
      <Link to="/">
        <Text size="5xl" weight="bold">
          {siteTitle}
        </Text>
      </Link>
      <DropdownSearch
        suggestionsCallback={suggestionsCallback}
        onSearch={(searchTerm) => history.push(`/search?term=${encodeURI(searchTerm)}&page=0`)}
        debounceTime={500}
        showImage
        data-testid="header-search"
        placeholder="Look For Group..."
      />
      {isLoggedIn ? (
        <Button onClick={() => dispatch(logoutUser())}>Logout</Button>
      ) : (
        <Link to="/login/" data-testid="header-login">
          <Button>Login</Button>
        </Link>
      )}
    </Grid>
  );
};

Header.propTypes = {
  siteTitle: PropTypes.string,
};

Header.defaultProps = {
  siteTitle: '',
};

export default Header;
