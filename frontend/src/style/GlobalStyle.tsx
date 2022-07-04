import { css, Global } from '@emotion/react';

const style = css`
  @font-face {
    font-family: 'BMYEONSUNG';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_one@1.0/BMYEONSUNG.woff') format('woff');
    font-weight: normal;
    font-style: normal;
  }
`;

const GlobalStyle = () => {
  return <Global styles={style} />;
};

export default GlobalStyle;
