module.exports = {
  ci: {
    collect: {
      staticDistDir: './dist',
      url: ['http://localhost:3000'],
      numberOfRuns: 1,
    },
    upload: {
      target: 'filesystem',
      outputDir: './lhci_reports',
      reportFilenamePattern: '%%PATHNAME%%-%%DATETIME%%-report.%%EXTENSION%%',
    },
  },
};
