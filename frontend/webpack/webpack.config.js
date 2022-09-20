const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const TsconfigPathsPlugin = require('tsconfig-paths-webpack-plugin');
const Dotenv = require('dotenv-webpack');
const InterpolateHtmlPlugin = require('interpolate-html-plugin');
require('dotenv').config();

module.exports = {
  mode: 'development',
  resolve: {
    extensions: ['.js', '.jsx', '.ts', '.tsx'],
    alias: {
      '@': path.resolve(__dirname, '../src/'),
    },
  },
  entry: {
    main: path.join(__dirname, '../src/index.tsx'),
  },
  output: {
    path: path.join(__dirname, '../dist'),
    publicPath: '/',
    filename: '[name].[contenthash].js',
  },
  module: {
    rules: [
      {
        test: /\.(png|jpg|jpeg|woff2)$/i,
        type: 'asset/resource',
      },
      {
        test: /\.(ttf|woff|woff2)$/i,
        type: 'asset/resource',
        generator: {
          filename: 'static/[name][ext][query]',
        },
      },
      {
        test: /\.(ts|tsx)$/,
        exclude: '/node_modules',
        loader: 'babel-loader',
      },
      {
        test: /\.svg$/i,
        use: ['@svgr/webpack'],
      },
    ],
  },
  plugins: [
    new Dotenv(),
    new TsconfigPathsPlugin(),
    new CleanWebpackPlugin(),
    new HtmlWebpackPlugin({
      template: path.resolve(__dirname, '../public/index.html'),
    }),
    new InterpolateHtmlPlugin({ PUBLIC_URL: '' }),
  ],
  devtool: 'source-map',
  optimization: {
    splitChunks: {
      chunks: 'all',
    },
  },
};
