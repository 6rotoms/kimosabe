import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { EditableTextArea, Layout, Grid, Tile, Tabs, Flex } from '../components';
import userService from '../services/userService';

const UserProfilePage = ({ username }) => {
  const [groupItems, setGroupItems] = useState('');

  const handleBioSave = (text) => {
    // TODO: Implement Redux action and API call for updating bio
    console.log(text);
  };
  useEffect(() => {
    const getGroups = async () => {
      const response = await userService.getGroups({ username });
      if (response.status !== 200) {
        return;
      }
      const newGroupItems = response.body.slice(0, 6).map((group) => (
        <Link key={group.groupId} className="relative w-16" to={`/group/${group.groupId}`}>
          <img className="w-16 rounded-lg" src={group.coverUrl} alt={group.coverUrl} />
        </Link>
      ));
      setGroupItems(newGroupItems);
    };

    getGroups();
  }, [username]);

  return (
    <Layout>
      <Flex justify="justify-center" align="items-center">
        <Grid rows="grid-rows-4" cols="grid-cols-4" gap="gap-4" className="h-97 w-full min-h-90">
          <div className="row-span-3 col-span-1 row-start-1 col-start-1">
            <Tile height="h-full" title="username" titleAlign="text-center"></Tile>
          </div>
          <div className="row-span-2 col-span-3 row-start-1 col-start-2">
            <Tile height="h-full">
              <EditableTextArea
                data-testid="user-bio"
                initialText={''}
                isToggleable={true}
                onSave={handleBioSave}
                charLimit={2500}
              />
            </Tile>
          </div>
          <div className="row-span-1 col-span-1 row-start-4 col-start-1">
            <Tile height="h-full">
              <Tabs tabNames={['Friends', 'Groups']}>
                <div data-testid="users-friends">Friends!</div>
                <Grid
                  cols="grid-cols-groups"
                  rows="grid-rows-o"
                  gap="gap-1"
                  justify="justify-center"
                  className="grid-flow-row overflow-auto"
                  data-testid="users-groups"
                >
                  {groupItems}
                </Grid>
              </Tabs>
            </Tile>
          </div>
          <div className="row-span-2 col-span-3 row-start-3 col-start-2">
            <Tile height="h-full"></Tile>
          </div>
        </Grid>
      </Flex>
    </Layout>
  );
};

export default UserProfilePage;
