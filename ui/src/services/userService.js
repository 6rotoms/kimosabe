const userService = {
  // TODO: Replace with real getGroups call...
  // eslint-disable-next-line no-unused-vars
  getGroups: async ({ username }) => {
    return {
      status: 200,
      body: [
        {
          coverUrl: '//images.igdb.com/igdb/image/upload/t_cover_big/co1ndn.jpg',
          groupName: 'Baldur’s Gate I: Enhanced Edition',
          groupId: 'some-string1',
        },
        {
          coverUrl: '//images.igdb.com/igdb/image/upload/t_cover_big/co1ndn.jpg',
          groupName: 'Really Long Title here to test flexbox and overlap',
          groupId: 'some-string2',
        },
        {
          coverUrl: '//images.igdb.com/igdb/image/upload/t_cover_big/co1ndn.jpg',
          groupName: 'bg3',
          groupId: 'some-string3',
        },
        {
          coverUrl: '//images.igdb.com/igdb/image/upload/t_cover_big/co1ndn.jpg',
          groupName: 'bg3',
          groupId: 'some-string4',
        },
        {
          coverUrl: '//images.igdb.com/igdb/image/upload/t_cover_big/co1ndn.jpg',
          groupName: '0123456789a123456789b123456789c1234',
          groupId: 'some-string5',
        },
        {
          coverUrl: '//images.igdb.com/igdb/image/upload/t_cover_big/co1ndn.jpg',
          groupName: 'bg3',
          groupId: 'some-string6',
        },
        {
          coverUrl: '//images.igdb.com/igdb/image/upload/t_cover_big/co1ndn.jpg',
          groupName: 'bg3',
          groupId: 'some-string7',
        },
      ],
    };
  },
};

export default userService;
