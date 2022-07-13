const { merge } = require('webpack-merge');
const path = require('path');

const common = require('./webpack.config');

module.exports = merge(common, {
  devServer: {
    port: 3000,
    historyApiFallback: true,
    open: true,
    hot: true,
    static: {
      directory: path.resolve(__dirname, '../public'),
      publicPath: '/',
    },
    devMiddleware: {
      stats: 'errors-only',
    },
  },
});
