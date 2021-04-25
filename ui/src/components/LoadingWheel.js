import React from 'react';
import Text from './Text';
import Flex from './Flex';

const LoadingWheel = () => (
  <>
    <Flex direction="flex-col" align="items-center" justify="justify-center" width="w-auto" height="h-auto">
      <div className="lds-ripple">
        <div></div>
        <div></div>
      </div>
      <Text>Loading...</Text>
    </Flex>
  </>
);

export default LoadingWheel;