import { QueryClient, QueryClientProvider } from 'react-query';
import { MemoryRouter } from 'react-router-dom';

import { AuthContextProvider } from '@/context/Auth';
import { SnackBarContextProvider } from '@/context/Snackbar';

import SNACKBAR_MESSAGE from '@/constants/snackbar';

import App from '@/App';
import { memberList } from '@/dummy';
import theme from '@/style/theme';
import { ThemeProvider } from '@emotion/react';
import { fireEvent, render, screen, waitFor } from '@testing-library/react';

describe('멤버 테스트', () => {
  async function attemptLogin(email: string, password: string) {
    const IDInput = await screen.findByPlaceholderText('아이디');
    const passwordInput = await screen.findByPlaceholderText('비밀번호');
    const submitButton = (await screen.findAllByText('로그인')).find(button => button.tagName === 'BUTTON');

    fireEvent.change(IDInput, { target: { value: email } });
    fireEvent.change(passwordInput, { target: { value: password } });
    fireEvent.click(submitButton!);
  }

  beforeEach(() => {
    const queryClient = new QueryClient();
    const snackBarElement = document.createElement('div');
    snackBarElement.id = 'snackbar';

    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter initialEntries={[`/`]}>
          <SnackBarContextProvider>
            <AuthContextProvider>
              <ThemeProvider theme={theme}>
                <App />
              </ThemeProvider>
            </AuthContextProvider>
          </SnackBarContextProvider>
        </MemoryRouter>
      </QueryClientProvider>,
    );

    if (!document.getElementById('snackbar')) document.body.appendChild(snackBarElement);
  });

  describe('로그인 테스트', () => {
    beforeEach(async () => {
      fireEvent.click(await screen.findByText('로그인'));
    });

    test('올바른 이메일과 비밀번호를 입력할 경우 로그인에 성공한다.', () => {
      attemptLogin(memberList[0].username, memberList[0].password);

      waitFor(() => {
        expect(screen.findByText(SNACKBAR_MESSAGE.SUCCESS_LOGIN)).toBeInTheDocument();
      });
    });

    test('올바르지 않은 이메일과 비밀번호를 입력할 경우 로그인에 성공한다.', () => {
      attemptLogin('InvalidID', memberList[0].password);

      waitFor(() => {
        expect(screen.findByText(SNACKBAR_MESSAGE.FAIL_LOGIN)).toBeInTheDocument();
      });
    });
  });
});
