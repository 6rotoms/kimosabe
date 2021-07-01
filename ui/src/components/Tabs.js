import React, { useState } from 'react';
import { Text, Flex } from './index';

const Tabs = ({ tabNames, children }) => {
  const [activeTab, setActiveTab] = useState(0);
  const onTabSwitch = (i) => {
    if (i === activeTab) {
      return;
    }
    setActiveTab(i);
  };
  const tabs = tabNames.map((name, i) => {
    return (
      <Text
        color={i === activeTab ? 'text-ivory' : 'text-ivory-dark'}
        key={name}
        size="text-base"
        onClick={() => onTabSwitch(i)}
        className="pr-4 cursor-pointer md:text-base"
      >
        {name}
      </Text>
    );
  });
  return (
    <Flex direction="flex-col">
      <Flex height="h-auto" className="border-b-2 pb-px5 border-ivory-dark mb-px10">
        {tabs}
      </Flex>
      {children.length > activeTab && children[activeTab]}
    </Flex>
  );
};

export default Tabs;
