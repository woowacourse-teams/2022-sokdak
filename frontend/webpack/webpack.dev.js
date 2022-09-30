const { merge } = require('webpack-merge');
const common = require('./webpack.config.js');
const { DefinePlugin } = require('webpack');
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;
const CopyWebpackPlugin = require('copy-webpack-plugin');
const WorkboxPlugin = require('workbox-webpack-plugin');

module.exports = merge(common, {
  mode: 'production',
  plugins: [
    new DefinePlugin({
      'process.env.API_URL': JSON.stringify('https://devwas.sokdaksokdak.com'),
      'process.env.MODE': JSON.stringify('production'),
    }),
    new BundleAnalyzerPlugin({
      openAnalyzer: false,
      analyzerMode: 'static',
      reportFilename: 'bundle-analyzer.html',
    }),
    new CopyWebpackPlugin({
      patterns: [{ from: './public/icons', to: './icons' }, './public/manifest.json'],
    }),
    new WorkboxPlugin.GenerateSW({
      clientsClaim: true,
      skipWaiting: true,
    }),
  ],
});
