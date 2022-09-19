import emotionReset from 'emotion-reset';

import '@/assets/fonts/BMHANNAAir/BMHANNAAir.ttf';
import '@/assets/fonts/BMHANNAAir/BMHANNAAir.woff';
import '@/assets/fonts/BMHANNAAir/BMHANNAAir.woff2';
import '@/assets/fonts/BMHANNAPro/BMHANNAPro.ttf';
import '@/assets/fonts/BMHANNAPro/BMHANNAPro.woff';
import '@/assets/fonts/BMHANNAPro/BMHANNAPro.woff2';
import '@/assets/fonts/BMYEONSUNG/BMYEONSUNG.ttf';
import '@/assets/fonts/BMYEONSUNG/BMYEONSUNG.woff';
import '@/assets/fonts/BMYEONSUNG/BMYEONSUNG.woff2';

import { css, Global, keyframes } from '@emotion/react';

const style = css`
  ${emotionReset}

  @font-face {
    font-family: 'BMYEONSUNG';
    src: url('static/BMYEONSUNG.woff2') format('woff2'), url('static/BMYEONSUNG.woff2') format('woff'),
      url('static/BMYEONSUNG.woff2') format('truetype');
    font-weight: normal;
    font-style: normal;
    font-display: optional;
  }

  @font-face {
    font-family: 'BMHANNAPro';
    src: url('static/BMHANNAPro.woff2') format('woff2'), url('static/BMHANNAPro.woff2') format('woff'),
      url('static/BMHANNAPro.woff2') format('truetype');
    font-weight: normal;
    font-style: normal;
    font-display: optional;
  }

  @font-face {
    font-family: 'BMHANNAAir';
    src: url('static/BMHANNAAir.woff2') format('woff2'), url('static/BMHANNAAir.woff2') format('woff'),
      url('static/BMHANNAAir.woff2') format('truetype');
    font-weight: normal;
    font-style: normal;
    font-display: optional;
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
