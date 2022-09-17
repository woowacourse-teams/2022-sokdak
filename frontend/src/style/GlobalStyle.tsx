import emotionReset from 'emotion-reset';

import { css, Global, keyframes } from '@emotion/react';

const style = css`
  ${emotionReset}

  @font-face {
    font-family: 'BMYEONSUNG';
    src: url(${require('@/assets/fonts/BMYEONSUNG/BMYEONSUNG.woff2')}) format('woff2'),
      url(${require('@/assets/fonts/BMYEONSUNG/BMYEONSUNG.woff')}) format('woff'),
      url(${require('@/assets/fonts/BMYEONSUNG/BMYEONSUNG.ttf')}) format('truetype');
    font-weight: normal;
    font-style: normal;
  }

  @font-face {
    font-family: 'BMHANNAPro';
    src: url(${require('@/assets/fonts/BMHANNAPro/BMHANNAPro.woff2')}) format('woff2'),
      url(${require('@/assets/fonts/BMHANNAPro/BMHANNAPro.woff')}) format('woff'),
      url(${require('@/assets/fonts/BMHANNAPro/BMHANNAPro.ttf')}) format('truetype');
    font-weight: normal;
    font-style: normal;
  }

  @font-face {
    font-family: 'BMHANNAAir';
    src: url(${require('@/assets/fonts/BMHANNAAir/BMHANNAAir.woff2')}) format('woff2'),
      url(${require('@/assets/fonts/BMHANNAAir/BMHANNAAir.woff')}) format('woff'),
      url(${require('@/assets/fonts/BMHANNAAir/BMHANNAAir.ttf')}) format('truetype');
    font-weight: normal;
    font-style: normal;
  }

  html,
  span,
  a {
    font-family: 'Noto Sans KR', sans-serif;
  }

  a {
    text-decoration: none;
    color: black;
  }

  input {
    border: none;
    outline: none;
    font-family: 'Noto Sans KR', sans-serif;
  }

  textarea {
    border: none;
    resize: none;
    outline: none;
    font-family: 'Noto Sans KR', sans-serif;
    line-height: 20px;
  }

  button {
    font-family: 'Noto Sans KR', sans-serif;
    border: none;
    cursor: pointer;
  }
`;

const GlobalStyle = () => {
  return <Global styles={style} />;
};

export const huduldul = keyframes`
  0% {
    transform: translateX(-5px);
  }
  20% {
    transform: translateX(5px);
  }
  40% {
    transform: translateX(-5px);
  }
  60% {
    transform: translateX(5px);
  }
  100% {
    transform: translateX(0px);
  }
`;

export const invalidInputAnimation = (props: { isAnimationActive?: boolean }) => css`
  animation: ${props.isAnimationActive ? huduldul : null} 0.3s;
`;

export default GlobalStyle;
