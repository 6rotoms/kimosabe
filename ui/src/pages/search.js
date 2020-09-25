import React from 'react';
import { useState } from 'react';
import { useSelector } from 'react-redux';
import GameService from '../services/gameService';

import '../styles/search.css';
import Layout from '../components/layout';
import SEO from '../components/seo';
import SearchResult from '../components/searchResult';

const SearchPage = () => {
  const [gameComponents, setGameComponents] = useState('');
  const searchTerm = useSelector(state => state.search.searchTerm);
  return (
    <Layout>
      <SEO title="Home" />
      <div className="search">
        <div
          onClick={async () => {
            const res = await GameService.gameSearch({
              searchTerm: searchTerm,
              pageNum: 0,
            });
            setGameComponents(
              res.body.map(game => <SearchResult key={game.key} {...game} />)
            );
          }}
          value="Search"
          className="search-button-temp input-button"
        >
          Search
        </div>
        {gameComponents}
      </div>
    </Layout>
  );
};

export default SearchPage;
