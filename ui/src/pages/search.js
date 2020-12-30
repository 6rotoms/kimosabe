import React, { useEffect, useState } from 'react';
import GameService from '../services/gameService';

import '../styles/search.css';
import SearchResult from '../components/SearchResult';

const SearchPage = ({ searchTerm, pageNum }) => {
  const [gameComponents, setGameComponents] = useState('');

  useEffect(() => {
    const updateGameComponents = async () => {
      const res = await GameService.gameSearch({
        searchTerm: searchTerm,
        pageNum: pageNum,
      });
      if (res.status === 200) {
        setGameComponents(res.body.map((game) => <SearchResult key={game.id} {...game} />));
      }
    };
    updateGameComponents();
  }, [searchTerm, pageNum]);

  return (
    <div data-testid="search-results" className="search">
      {gameComponents}
    </div>
  );
};

export default SearchPage;
