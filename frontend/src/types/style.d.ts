import '@emotion/react';

declare module '@emotion/react' {
  interface Theme {
    colors: {
      main: string;
      sub: string;
      gray_50: string;
      gray_100: string;
      gray_200: string;
      gray_300: string;
      red_100: string;
    };
  }
}
