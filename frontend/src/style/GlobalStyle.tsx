import emotionReset from 'emotion-reset';

import BMHANNA_AIR_TTF from '@/assets/fonts/BMHANNAAir/BMHANNAAir.ttf';
import BMHANNA_AIR_WOFF from '@/assets/fonts/BMHANNAAir/BMHANNAAir.woff';
import BMHANNA_AIR_WOFF2 from '@/assets/fonts/BMHANNAAir/BMHANNAAir.woff2';
import BMHANNA_PRO_TTF from '@/assets/fonts/BMHANNAPro/BMHANNAPro.ttf';
import BMHANNA_PRO_WOFF from '@/assets/fonts/BMHANNAPro/BMHANNAPro.woff';
import BMHANNA_PRO_WOFF2 from '@/assets/fonts/BMHANNAPro/BMHANNAPro.woff2';
import BMYEONSUNG_TTF from '@/assets/fonts/BMYEONSUNG/BMYEONSUNG.ttf';
import BMYEONSUNG_WOFF from '@/assets/fonts/BMYEONSUNG/BMYEONSUNG.woff';
import BMYEONSUNG_WOFF2 from '@/assets/fonts/BMYEONSUNG/BMYEONSUNG.woff2';

import { css, Global, keyframes } from '@emotion/react';

const style = css`
  ${emotionReset}

  @font-face {
    font-family: 'BMYEONSUNG';
    src: url('${BMYEONSUNG_WOFF2}') format('woff2'), url('${BMYEONSUNG_WOFF}') format('woff'),
      url('${BMYEONSUNG_TTF}') format('truetype');
    font-weight: normal;
    font-style: normal;
    font-display: optional;
  }

  @font-face {
    font-family: 'BMHANNAPro';
    src: url('${BMHANNA_PRO_WOFF2}') format('woff2'), url('${BMHANNA_PRO_WOFF}') format('woff'),
      url('${BMHANNA_PRO_TTF}') format('truetype');
    font-weight: normal;
    font-style: normal;
    font-display: optional;
  }

  @font-face {
    font-family: 'BMHANNAAir';
    src: url('${BMHANNA_AIR_WOFF2}') format('woff2'), url('${BMHANNA_AIR_WOFF}') format('woff'),
      url('${BMHANNA_AIR_TTF}') format('truetype');
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
