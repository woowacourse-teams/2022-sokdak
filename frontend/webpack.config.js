const path = require('path');
const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const ReactRefreshWebpackPlugin = require('@pmmmwh/react-refresh-webpack-plugin');

const config = ({ isDev }) => ({
  mode: isDev ? 'development' : 'production',
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src'),
    },
    extensions: ['.js', '.jsx', '.ts', '.tsx'],
  },
  entry: {
    main: './src/index',
  },
  output: {
    path: path.join(__dirname, 'dist'),
    publicPath: '/',
    filename: '[name].js',
  },
  module: {
    rules: [
      {
        test: /\.(png|jpg|gif)$/,
        loader: 'url-loader',
        options: {
          name: '[name].[ext]?[hash]',
          limit: 5000,
        },
      },
      {
        test: /\.(png|jpg|gif)$/,
        loader: 'file-loader',
        options: {
          publicPath: './public/',
          name: '[name].[ext]?[hash]',
        },
      },
      {
        test: /\.(js|jsx)$/,
        exclude: '/node_modules',
        loader: 'babel-loader',
        options: {
          presets: [
            ['@babel/preset-env', { targets: { esmodules: true, browsers: ['last 2 versions'] } }],
            '@babel/preset-react',
          ],
          plugins: ['@emotion', isDev && 'react-refresh/babel'].filter(Boolean),
        },
      },
      { test: /\.tsx?$/, loader: 'ts-loader' },
      {
        test: /\.svg$/i,
        use: ['@svgr/webpack'],
      },
    ],
  },
  plugins: [
    new webpack.DefinePlugin({
      VERSION: JSON.stringify('v0.1.0'),
    }),
    new CleanWebpackPlugin(),
    new HtmlWebpackPlugin({
      template: './public/index.html',
    }),
    new ReactRefreshWebpackPlugin(),
  ],
  devServer: {
    port: 3000,
    historyApiFallback: true,
    open: true,
    hot: true,
    static: {
      directory: path.resolve(__dirname, 'public'),
      publicPath: '/',
    },
    devMiddleware: {
      stats: 'errors-only',
    },
  },
});

module.exports = (env, argv) => config({ isDev: argv.mode === 'development' });
