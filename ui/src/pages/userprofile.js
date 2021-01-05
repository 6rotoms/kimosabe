import React, { useEffect, useState } from 'react';
import history from '../history';
import Tile from '../components/Tile';
import UserBiography from '../components/UserBiography';
import userService from '../services/userService';
import '../styles/userprofile.css';
import Layout from '../components/Layout';

const UserProfilePage = ({ username }) => {
  const [groupItems, setGroupItems] = useState('');

  useEffect(() => {
    const getGroups = async () => {
      const response = await userService.getGroups({ username });
      if (response.status !== 200) {
        return;
      }
      const newGroupItems = response.body.map((group) => (
        <div key={group.groupId} className="user-group" onClick={() => history.push(`/group/${group.groupId}`)}>
          <img className="group-coverart" src={group.coverUrl} alt={group.coverUrl} />
          <div className="user-group_title">
            {group.groupName.length > 34 ? `${group.groupName.substring(0, 32)}...` : group.groupName}
          </div>
        </div>
      ));
      setGroupItems(newGroupItems);
    };

    getGroups();
  }, [username]);

  return (
    <Layout>
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
          <Tile title="Groups" titleAlign="left">
            <div className="groups-container" data-testid="users-groups">
              {groupItems}
            </div>
          </Tile>
        </div>
      </div>
    </Layout>
  );
};

export default UserProfilePage;
