import React from 'react';

const SearchResult = props => {
  return (
    <div className="search-result">
      <div className="search-result__cover-container">
        <img src={props.coverUrl} className="search-result__cover" />
      </div>
      <div className="search-result-text">
        <h2 className="search-result-text__title">{props.name}</h2>
        <p className="search-result-text__desc">{props.summary}</p>
      </div>
    </div>
  );
};

export default SearchResult;
