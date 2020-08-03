const layoutQuery = () => ({
  site: {
    siteMetadata: {
      title: 'Gatsby Default Starter',
    },
  },
});

const SEOQuery = () => ({
  site: {
    siteMetadata: {
      title: 'Gatsby Default Starter',
      description:
        'Kick off your next, great Gatsby project with this default starter. This barebones starter ships with the main Gatsby configuration files you might need.',
      author: '@gatsbyjs',
    },
  },
});

module.exports = {
  layoutQuery,
  SEOQuery,
};
