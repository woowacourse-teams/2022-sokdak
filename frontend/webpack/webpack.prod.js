const { merge } = require('webpack-merge');
const common = require('./webpack.config.js');
const { DefinePlugin } = require('webpack');

require('dotenv').config();

module.exports = merge(common, {
  mode: 'production',
  plugins: [
    new DefinePlugin({
      'process.env.API_URL': JSON.stringify('http://192.168.1.241'),
      'process.env.MODE': JSON.stringify('PRODUCTION'),
    }),
  ],
});
