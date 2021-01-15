import React, { useRef, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';
import { useDebounce } from '../utils/helpers';
import Input from './Input';
import Dropdown from './Dropdown';

const DropdownSearch = ({ suggestionsCallback, onSearch, debounceTime, showImage, ...other }) => {
  const [suggestions, setSuggestions] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const alreadyRan = useRef(false);
  const debouncedSearchTerm = useDebounce(searchTerm, debounceTime);

  useEffect(() => {
    let mounted = true;

    if (alreadyRan.current) {
      if (!debouncedSearchTerm || !/\S/.test(debouncedSearchTerm)) return;
      const promise = suggestionsCallback(debouncedSearchTerm);
      promise.then((suggestionsData) => {
        if (mounted) {
          const suggestionComponents = suggestionsData.map((suggestion) => (
            <Link to={suggestion.link} key={suggestion.id} className="suggestion-container w-auto">
              <div className="suggestion flex border-b border-orange">
                {showImage && (
                  <img
                    data-testid="suggestion-img"
                    alt={suggestion.imgUrl}
                    src={suggestion.imgUrl}
                    className="h-16 rounded-lg m-4"
                  />
                )}
                <div className="suggestiontitle flex items-center m-4 ml-0">{suggestion.text}</div>
              </div>
            </Link>
          ));
          setSuggestions(suggestionComponents);
        }
      });
    } else {
      alreadyRan.current = true;
    }
    return () => (mounted = false);
  }, [suggestionsCallback, debouncedSearchTerm, showImage]);

  return (
    <div data-testid="dropdown" className="w-5/12">
      <Dropdown
        content={suggestions}
        footer={`Results for "${searchTerm}"`}
        visible={searchTerm && /\S/.test(searchTerm)}
        data-testid="suggestions"
      >
        <Input
          type="text"
          data-testid="searchbar"
          {...other}
          onChange={(e) => {
            e.preventDefault();
            setSearchTerm(e.target.value);
          }}
          onKeyUp={(e) => {
            if (e.keyCode === 13) {
              e.preventDefault();
              onSearch(searchTerm);
            }
          }}
        />
      </Dropdown>
    </div>
  );
};

DropdownSearch.propTypes = {
  // Callback used to fetch data for the suggestions
  // Returned data should be in this format:
  // {
  //   link: string - link to redirect to on click
  //   text: string - text to display for each suggestion
  //   imgUrl: string - url to image icon
  // }
  suggestionsCallback: PropTypes.func.isRequired,
  // function called when search is invoked by enter key
  onSearch: PropTypes.func,
  // time interval to wait between calls to suggestionsCallback
  debounceTime: PropTypes.number,
  // should images be shown for each suggestion
  showImage: PropTypes.bool,
};

export default DropdownSearch;
