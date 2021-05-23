import React, { useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';
import ReactPaginate from 'react-paginate';

import gameService from '../services/gameService';
import SearchResult from '../components/SearchResult';
import { Layout, Grid } from '../components';

const SearchPage = ({ searchTerm, pageNum }) => {
  const [gameComponents, setGameComponents] = useState('');
  const [numPages, setNumPages] = useState('');
  const history = useHistory();

  useEffect(() => {
    let mounted = true;

    const getTotalNumberOfPages = async () => {
      const res = await gameService.getSearchInfo({
        searchTerm: searchTerm,
      });

      if (res.status === 200 && mounted) {
        setNumPages(res.body.maxNumPages + 1);
      }
    };
    getTotalNumberOfPages();

    return () => (mounted = false);
  }, [searchTerm]);

  useEffect(() => {
    let mounted = true;

    const updateGameComponents = async () => {
      const res = await gameService.gameSearch({
        searchTerm: searchTerm,
        pageNum: pageNum,
      });
      if (res.status === 200 && mounted) {
        setGameComponents(res.body.map((game) => <SearchResult key={game.id} {...game} />));
      }
    };
    updateGameComponents();

    return () => (mounted = false);
  }, [searchTerm, pageNum]);

  const onPageChange = (e) => {
    history.push(`/search?term=${encodeURI(searchTerm)}&page=${e.selected}`);
  };

  return (
    <Layout>
      <Grid rows="grid-rows-25" cols="grid-cols-1" gap="gap-6" className="pt-2 justify-items-center">
        {gameComponents}
      </Grid>
      <ReactPaginate
        previousLabel="<<"
        nextLabel=">>"
        breakLabel="..."
        pageCount={numPages}
        marginPagesDisplayed={2}
        pageRangeDisplayed={3}
        onPageChange={onPageChange}
        containerClassName="justify-center flex mb-2"
        subContainerClassName="pages pagination"
        breakClassName="text-ivory mx-1 p-2"
        pageClassName="text-ivory mx-1 p-2"
        pageLinkClassName="cursor-pointer outline-none"
        activeClassName="underline"
        activeLinkClassName="cursor-pointer outline-none"
        nextClassName="text-ivory mx-1 p-2"
        nextLinkClassName="cursor-pointer outline-none"
        previousClassName="text-ivory mx-1 p-2"
        previousLinkClassName="cursor-pointer outline-none"
      />
    </Layout>
  );
};

export default SearchPage;
