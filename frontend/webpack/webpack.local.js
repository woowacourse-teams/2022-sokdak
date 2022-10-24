const { merge } = require('webpack-merge');
const path = require('path');
const common = require('./webpack.config');
const { DefinePlugin } = require('webpack');
const ReactRefreshWebpackPlugin = require('@pmmmwh/react-refresh-webpack-plugin');
const Dotenv = require('dotenv-webpack');

require('dotenv').config();

module.exports = merge(common, {
  mode: 'development',
  devtool: 'eval-cheap-source-map',
  plugins: [
    new DefinePlugin({
      'process.env.API_URL': JSON.stringify(process.env.API_URL),
      'process.env.MODE': JSON.stringify(process.env.MODE),
    }),
    new ReactRefreshWebpackPlugin(),
    new Dotenv(),
  ],
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
  optimization: {
    minimize: false,
  },
});
