const { merge } = require('webpack-merge');
const common = require('./webpack.config.js');
const { DefinePlugin } = require('webpack');
const WorkboxPlugin = require('workbox-webpack-plugin');
const { ESBuildMinifyPlugin } = require('esbuild-loader');

module.exports = merge(common, {
  mode: 'production',
  plugins: [
    new DefinePlugin({
      'process.env.API_URL': JSON.stringify('https://was.sokdaksokdak.com'),
      'process.env.MODE': JSON.stringify('production'),
    }),
    new WorkboxPlugin.GenerateSW({
      clientsClaim: true,
      skipWaiting: true,
    }),
  ],
  optimization: {
    minimizer: [
      new ESBuildMinifyPlugin({
        target: 'esnext',
      }),
    ],
  },
});
