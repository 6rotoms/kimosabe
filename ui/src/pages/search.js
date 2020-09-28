import React from 'react';
import { useState } from 'react';
import { useSelector } from 'react-redux';
import GameService from '../services/gameService';

import '../styles/search.css';
import SearchResult from '../components/searchResult';

const SearchPage = () => {
  const [gameComponents, setGameComponents] = useState('');
  const searchTerm = useSelector((state) => state.search.searchTerm);
  return (
    <div className="search">
      <div
        onClick={async () => {
          const res = await GameService.gameSearch({
            searchTerm: searchTerm,
            pageNum: 0,
          });
          console.log(res);
          if (res.status === 200) {
            setGameComponents(res.body.map((game) => <SearchResult key={game.key} {...game} />));
          }
        }}
        value="Search"
        className="search-button-temp input-button"
      >
        Search
      </div>
      {gameComponents}
    </div>
  );
};

export default SearchPage;
