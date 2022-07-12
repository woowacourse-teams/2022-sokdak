import emotionReset from 'emotion-reset';

import { css, Global } from '@emotion/react';

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

  @font-face {
    font-family: 'BMHANNAAir';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_four@1.0/BMHANNAAir.woff') format('woff');
    font-weight: normal;
    font-style: normal;
  }

  html {
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
    border: none;
  }
`;

const GlobalStyle = () => {
  return <Global styles={style} />;
};

export default GlobalStyle;
