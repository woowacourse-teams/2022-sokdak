import { MemoryRouter } from 'react-router-dom';

import App from '@/App';
import theme from '@/style/theme';
import { ThemeProvider } from '@emotion/react';
import { render, screen } from '@testing-library/react';

test('등록되지 않은 URL에 접속시 NotFoundPage가 렌더링 된다.', () => {
  const badRoute = '/some/bad/route';

  render(
    <ThemeProvider theme={theme}>
      <MemoryRouter initialEntries={[badRoute]}>
        <App />
      </MemoryRouter>
    </ThemeProvider>,
  );

  expect(screen.getByText('페이지를 찾을 수 없습니다.')).toBeInTheDocument();
});
