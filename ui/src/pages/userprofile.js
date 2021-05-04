import React, { useEffect, useState } from 'react';
import { Link, useParams, useHistory } from 'react-router-dom';
import { Layout, Grid, Tile, Tabs, Flex, Text } from '../components';
import userService from '../services/userService';

const UserProfilePage = () => {
  const { username } = useParams();
  const history = useHistory();
  const [groupItems, setGroupItems] = useState('');
  const [friendItems, setFriendItems] = useState('');
  const [isLoading, setLoading] = useState(true);
  const [userData, setUserData] = useState({});

  useEffect(() => {
    let mounted = true;

    const getFriends = async () => {
      const response = await userService.getFriends({ username });
      if (response.status !== 200) {
        return;
      }
      const newFriendItems = response.body
        .slice(0, 6)
        .map((friend) => <div key={friend.username}>{friend.username}</div>);
      if (mounted) {
        setFriendItems(newFriendItems);
      }
    };

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
      if (mounted) {
        setGroupItems(newGroupItems);
      }
    };

    const getUserData = async () => {
      const newUserData = await userService.getUserData({ username });
      if (newUserData.status !== 200) {
        history.push('/404');
        return;
      }
      if (mounted) {
        setUserData(newUserData.body);
      }
    };

    const getAllData = async () => {
      await Promise.all([getUserData() , getFriends(), getGroups()]);
      if (mounted) {
        setLoading(false);
      }
    };

    getAllData();

    return () => (mounted = false);
  }, [username]);

  return (
    <Layout isLoading={isLoading}>
      <Flex justify="justify-center" align="items-center">
        <Grid rows="grid-rows-4" cols="grid-cols-4" gap="gap-4" className="w-full h-97 min-h-90">
          <div className="row-span-3 col-span-1 row-start-1 col-start-1">
            <Tile height="h-full">
              <Flex direction="flex-col">
                <Flex height="h-auto" align="items-center" className="border-b-2 pb-px10 mb-px10 border-ivory-dark">
                  <img
                    src="https://qph.fs.quoracdn.net/main-qimg-2b21b9dd05c757fe30231fac65b504dd"
                    className="object-cover w-16 h-16 rounded-full"
                  />
                  <Flex direction="flex-col" className="justify-center text-center">
                    <Text size="text-2xl">{username}</Text>
                    <Text size="text-sm">Last on: {new Date(userData.lastLogin).toDateString()}</Text>
                  </Flex>
                </Flex>
                <Text className="pt-2 pb-2 font-bold">
                  Age:
                  <Text className="float-right">{userData.age}</Text>
                </Text>
                <Text className="pb-2 font-bold">
                  Gender:
                  <Text className="float-right">{userData.gender}</Text>
                </Text>
                <Text className="pb-2 font-bold">
                  Location:
                  <Text className="float-right">{userData.location}</Text>
                </Text>
              </Flex>
            </Tile>
          </div>
          <div className="row-span-2 col-span-3 row-start-1 col-start-2">
            <Tile height="h-full" title="About" titleAlign="text-center">
              <Flex
                data-testid="display-text"
                className="flex-1 overflow-y-auto break-all whitespace-pre-line px-px5 py-px10 text-ivory"
              >
                {userData.biography}
              </Flex>
            </Tile>
          </div>
          <div className="row-span-1 col-span-1 row-start-4 col-start-1">
            <Tile height="h-full">
              <Tabs tabNames={['Friends', 'Groups']}>
                <div data-testid="users-friends">{friendItems}</div>
                <Flex className="justify-center">
                  <Grid
                    cols="grid-cols-groups"
                    rows="grid-rows-o"
                    gap="gap-2"
                    justify="justify-left"
                    className="overflow-auto grid-flow-row w-52"
                    data-testid="users-groups"
                  >
                    {groupItems}
                  </Grid>
                </Flex>
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
