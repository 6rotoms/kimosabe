import React, { useEffect, useState } from 'react';
import gameService from '../services/gameService';
import ReactPaginate from 'react-paginate';
import history from '../history';

import SearchResult from '../components/SearchResult';
import Layout from '../components/Layout';

const SearchPage = ({ searchTerm, pageNum }) => {
  const [gameComponents, setGameComponents] = useState('');
  const [numPages, setNumPages] = useState('');

  useEffect(() => {
    const getTotalNumberOfPages = async () => {
      const res = await gameService.getSearchInfo({
        searchTerm: searchTerm,
      });

      if (res.status === 200) {
        setNumPages(res.body.maxNumPages + 1);
      }
    };
    getTotalNumberOfPages();
  }, [searchTerm]);

  useEffect(() => {
    const updateGameComponents = async () => {
      const res = await gameService.gameSearch({
        searchTerm: searchTerm,
        pageNum: pageNum,
      });
      if (res.status === 200) {
        setGameComponents(res.body.map((game) => <SearchResult key={game.id} {...game} />));
      }
    };
    updateGameComponents();
  }, [searchTerm, pageNum]);

  const onPageChange = (e) => {
    history.push(`/search?term=${encodeURI(searchTerm)}&page=${e.selected}`);
  };

  return (
    <Layout>
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
    </Layout>
  );
};

export default SearchPage;
