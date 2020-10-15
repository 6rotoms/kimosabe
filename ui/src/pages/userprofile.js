import React from 'react';
import Tile from '../components/Tile';
import UserBiography from '../components/UserBiography';
import '../styles/userprofile.css';

const UserProfilePage = () => {
  return (
    <div className="userprofile-container">
      <div className="col1">
          <Tile></Tile>
          <Tile title="Friends" titleAlign="left"></Tile>
      </div>
      <div className="col2">
          <UserBiography isToggleable={true} />
          <Tile></Tile>
      </div>
      <div className="col3">
        <Tile title="Groups" titleAlign="left"></Tile>
      </div>
    </div>
  );
};

export default UserProfilePage;
