const { merge } = require('webpack-merge');
const common = require('./webpack.config.js');
const { DefinePlugin } = require('webpack');

module.exports = merge(common, {
  mode: 'production',
  plugins: [
    new DefinePlugin({
      'process.env.API_URL': JSON.stringify('https://was.sokdaksokdak.com'),
      'process.env.MODE': JSON.stringify('production'),
    }),
  ],
});
