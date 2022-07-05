import { css, Global } from '@emotion/react';
import emotionReset from 'emotion-reset';

const style = css`
  ${emotionReset}

  @font-face {
    font-family: 'BMYEONSUNG';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_one@1.0/BMYEONSUNG.woff') format('woff');
    font-weight: normal;
    font-style: normal;
  }

  @font-face {
    font-family: 'BMHANNAPro';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_seven@1.0/BMHANNAPro.woff') format('woff');
    font-weight: normal;
    font-style: normal;
  }

  a {
    text-decoration: none;
    color: black;
  }
`;

const GlobalStyle = () => {
  return <Global styles={style} />;
};

export default GlobalStyle;
