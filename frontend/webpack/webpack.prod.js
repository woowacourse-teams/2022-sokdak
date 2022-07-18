const { merge } = require('webpack-merge');
const common = require('./webpack.config.js');

require('dotenv').config();

module.exports = merge(common, {
  mode: 'production',
  plugins: [
    new DefinePlugin({
      'process.env.API_URL': JSON.stringify(process.env.API_URL),
      'process.env.MODE': JSON.stringify(process.env.MODE),
    }),
  ],
});
