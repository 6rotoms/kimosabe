import React, { useEffect } from 'react';
import { useState, useCallback } from 'react';
import GameService from '../services/gameService';
import ReactPaginate from 'react-paginate';

import '../styles/search.css';
import SearchResult from '../components/searchResult';

const SearchPage = ({ searchTerm, pageNum }) => {
  const [gameComponents, setGameComponents] = useState('');
  const [numPages, setNumPages] = useState('');

  const updateGameComponents = useCallback(
    async (pageNum) => {
      const res = await GameService.gameSearch({
        searchTerm: searchTerm,
        pageNum: pageNum,
      });
      if (res.status === 200) {
        setGameComponents(res.body.map((game) => <SearchResult key={game.id} {...game} />));
      }
    },
    [searchTerm],
  );

  const getTotalNumberOfPages = useCallback(async () => {
    const res = await GameService.getSearchInfo({
      searchTerm: searchTerm,
    });

    if (res.status === 200) {
      setNumPages(res.body.maxNumPages + 1);
    }
  }, [searchTerm]);

  const onPageChange = (e) => {
    updateGameComponents(e.selected);
  };

  useEffect(() => {
    updateGameComponents(pageNum);
    getTotalNumberOfPages();
  }, [updateGameComponents, pageNum, getTotalNumberOfPages]);

  return (
    <div data-testid="search-results" className="search">
      {gameComponents}
      <ReactPaginate
        previousLabel="<<"
        nextLabel=">>"
        breakLabel="..."
        pageCount={numPages}
        marginPagesDisplayed={2}
        pageRangeDisplayed={3}
        onPageChange={onPageChange}
        containerClassName="pagination"
        subContainerClassName="pages pagination"
        breakClassName="pagination-break"
        pageClassName="pagination-page"
        pageLinkClassName="pagination-page-link"
        activeClassName="pagination-active"
        activeLinkClassName="pagination-active-link"
        nextClassName="pagination-next"
        nextLinkClassName="pagination-next-link"
        previousClassName="pagination-previous"
        previousLinkClassName="pagination-previous-link"
      />
    </div>
  );
};

export default SearchPage;
