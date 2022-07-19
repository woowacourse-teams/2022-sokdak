import '@emotion/react';

declare module '@emotion/react' {
  interface Theme {
    colors: {
      main: string;
      sub: string;
      gray_50: string;
      gray_100: string;
      gray_150: string;
      gray_200: string;
      gray_300: string;
      gray_900: string;
      red_100: string;
      red_200: string;
      pink_300: string;
    };
  }
}
