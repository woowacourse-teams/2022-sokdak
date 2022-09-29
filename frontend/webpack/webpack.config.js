const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const { ESBuildMinifyPlugin } = require('esbuild-loader');
const { DefinePlugin } = require('webpack');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const WorkboxPlugin = require('workbox-webpack-plugin');

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
    chunkFilename: '[name].[contenthash].chunk.js',
    clean: true,
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
        test: /[sS]tyles?\.tsx?$/,
        loader: 'babel-loader',
      },
      {
        test: /\.tsx?$/,
        loader: 'esbuild-loader',
        options: {
          loader: 'tsx',
          target: 'esnext',
        },
      },
      {
        test: /\.svg$/i,
        use: ['@svgr/webpack'],
      },
    ],
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: path.resolve(__dirname, '../public/index.html'),
      templateParameters: {
        PUBLIC_URL: '',
      },
    }),
    new DefinePlugin({
      'process.env.IMAGE_API_URL': JSON.stringify('https://img.sokdaksokdak.com/images/'),
    }),
    new CopyWebpackPlugin({
      patterns: [{ from: './public/icons', to: './icons' }, './public/manifest.json'],
    }),
    new WorkboxPlugin.GenerateSW({
      clientsClaim: true,
      skipWaiting: true,
    }),
  ],
  devtool: 'source-map',
  optimization: {
    splitChunks: {
      chunks: 'all',
      cacheGroups: {
        defaultVendors: {
          test: /[\\/]node_modules[\\/]/,
          name: 'vendors',
        },
      },
    },
    minimizer: [
      new ESBuildMinifyPlugin({
        target: 'esnext',
      }),
    ],
  },
};
