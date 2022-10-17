import theme from '@/style/theme';
import '@emotion/react';

declare module '@emotion/react' {
  interface Theme {
    colors: { [key in keyof typeof theme.colors]: string };
  }
}
