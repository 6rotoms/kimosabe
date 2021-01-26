import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import {
  EditableTextArea,
  Layout,
  Grid,
  Cell,
  Tile,
  Tabs,
  Flex,
} from '../components';
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
      <Flex justify="center" align="center">
        <Grid rows={4} cols={4} gap={4} className="h-97 w-full min-h-90">
          <Cell rowSpan={3} colSpan={1} rowStart={1} colStart={1}>
            <Tile pd={6} height="full"></Tile>
          </Cell>
          <Cell rowSpan={2} colSpan={3} rowStart={1} colStart={2}>
            <Tile pd={6} height="full">
              <EditableTextArea initialText={''} isToggleable={true} onSave={handleBioSave} charLimit={2500} />
            </Tile>
          </Cell>
          <Cell rowSpan={1} colSpan={1} rowStart={4} colStart={1}>
            <Tile height="full" pd={6}>
              <Tabs tabNames={['Friends', 'Groups']}>
                <div>Friends!</div>
                <Grid cols="groups" rows="o" gap={1} justify="center" className="grid-flow-row overflow-auto">
                  {groupItems}
                </Grid>
              </Tabs>
            </Tile>
          </Cell>
          <Cell rowSpan={2} colSpan={3} rowStart={3} colStart={2}>
            <Tile pd={4} height="full"></Tile>
          </Cell>
        </Grid>
      </Flex>
    </Layout>
  );
};

export default UserProfilePage;
