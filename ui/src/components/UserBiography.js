import React from 'react';
import Tile from './Tile';
import EditableTextArea from './EditableTextArea';

const UserBiography = ({ isToggleable, initialBio = ''}) => {
  const handleSave = (text) => {
    // TODO: Implement Redux action and API call for updating bio
    console.log(text);
  };

  return (
    <Tile>
      <EditableTextArea
        initialText={initialBio}
        isToggleable={isToggleable}
        onSave={handleSave}
        charLimit={2500}
      />
    </Tile>
  );
};

export default UserBiography;
