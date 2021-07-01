import React from 'react';
import { Tile, Text, Flex } from './index';

const SearchResult = ({ name, summary, coverUrl }) => {
  return (
    <Tile pd="p-3" height="h-32" width="w-11/12" className="justify-center" data-testid="search-result">
      <Flex>
        <img src={coverUrl} alt="" className="inline h-full" />
        <div className="overflow-hidden">
          <Text size="text-base md:text-xl" className="inline-block w-full px-5">
            {name}
          </Text>
          <Text size="text-xs md:text-base" className="inline-block px-5">
            {summary.length > 380 ? `${summary.slice(0, 377)}...` : summary}
          </Text>
        </div>
      </Flex>
    </Tile>
  );
};
export default SearchResult;
