import React, { useState } from 'react';
import {
  Text,
  Flex,
} from './index';

const Tabs = ({tabNames, children}) => {
  const [activeTab, setActiveTab] = useState(0);
  const onTabSwitch = (i) => {
    if (i === activeTab) {
      return;
    }
    setActiveTab(i);
  };
  const tabs = tabNames.map((name, i) => {
    let color = 'ivory-dark';
    if (i === activeTab) {
        color= 'ivory';
    }
    return <Text
      color={color}
      key={name}
      size="xs"
      onClick={() => onTabSwitch(i)}
      className="pr-4 cursor-pointer md:text-base">
        {name}
    </Text>;
  });
  return <Flex direction="col">
    <Flex height="auto pb-px5 border-ivory-dark border-b-2 mb-px10">
      {tabs}
    </Flex>
    {children.length > activeTab && children[activeTab]}
  </Flex>;
};

export default Tabs;