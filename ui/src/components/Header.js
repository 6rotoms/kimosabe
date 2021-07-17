/* eslint-disable tailwind/class-order */
import { Link, useHistory } from 'react-router-dom';
import PropTypes from 'prop-types';
import React, { useEffect } from 'react';
import gameService from '../services/gameService';
import { Grid, DropdownSearch, Button, Text } from './index';
import { useAsync, useLogin } from '../hooks';
import authService from '../services/authService';

const Header = () => {
  const [loggedIn, setLoggedIn] = useLogin();
  const { loading, triggerCall, response, error } = useAsync(() => authService.logout(), { runOnMount: false });
  const history = useHistory();

  useEffect(() => {
    if (loading || error) return;
    if (response) {
      setLoggedIn(false);
      history.push('/');
    }
  }, [loading, response, error]);

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
    <Grid rows="grid-rows-1" cols="grid-cols-header" gap="gap-4" className="bg-orange p-4">
      <Link to="/" className="text-center">
        <img src="/logo128.png" width="48px" className="inline" />
        <Text size="text-5xl" weight="text-bold" className="hidden md:inline align-middle">
          kimosabe
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
      {loggedIn ? (
        <Button onClick={() => triggerCall()}>Logout</Button>
      ) : (
        <Link to="/login/" data-testid="header-login">
          <Button>
            <Text>Login</Text>
          </Button>
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
