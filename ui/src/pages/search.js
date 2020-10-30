import React, { useEffect } from 'react';
import { useState, useCallback } from 'react';
import GameService from '../services/gameService';

import '../styles/search.css';
import SearchResult from '../components/SearchResult';

const SearchPage = ({ searchTerm, pageNum }) => {
  const [gameComponents, setGameComponents] = useState('');

  const updateGameComponents = useCallback(async () => {
    const res = await GameService.gameSearch({
      searchTerm: searchTerm,
      pageNum: pageNum,
    });
    if (res.status === 200) {
      setGameComponents(res.body.map((game) => <SearchResult key={game.id} {...game} />));
    }
  }, [searchTerm, pageNum]);

  useEffect(() => {
    updateGameComponents();
  }, [updateGameComponents]);

  return (
    <div data-testid="search-results" className="search">
      {gameComponents}
    </div>
  );
};

export default SearchPage;
