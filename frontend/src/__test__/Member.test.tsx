import { QueryClient, QueryClientProvider } from 'react-query';
import { MemoryRouter } from 'react-router-dom';

import { AuthContextProvider } from '@/context/Auth';
import { SnackBarContextProvider } from '@/context/Snackbar';

import SNACKBAR_MESSAGE from '@/constants/snackbar';

import App from '@/App';
import { memberList, validMemberEmail } from '@/dummy';
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

  describe('회원가입 테스트', () => {
    beforeEach(async () => {
      fireEvent.click(await screen.findByText('로그인'));
      fireEvent.click(await screen.findByText('간편 회원가입하기'));
    });

    test('올바른 양식을 입력하면 회원가입에 성공한다.', async () => {
      const targetMemberForm = validMemberEmail.find(member => !member.isSignedUp)!;

      const emailInput = await screen.findByPlaceholderText('이메일');
      fireEvent.change(emailInput, { target: { value: targetMemberForm.email } });
      fireEvent.click(await screen.findByText('인증번호 받기'));

      const verificationCodeInput = await screen.findByPlaceholderText('인증번호');
      fireEvent.change(verificationCodeInput, { target: { value: targetMemberForm.code } });
      fireEvent.click(await screen.findByText('확인'));

      const IDInput = await screen.findByPlaceholderText('아이디');
      const [IDsubmitButton, nicknameSubmitButton] = await screen.findAllByText('중복 확인');
      fireEvent.change(IDInput, { target: { value: 'validID' } });
      fireEvent.click(IDsubmitButton);

      const nickNameInput = await screen.findByPlaceholderText('닉네임');
      fireEvent.change(nickNameInput, { target: { value: 'validNickname' } });
      fireEvent.change(nicknameSubmitButton);

      const validPassword = 'validPassword123!';
      const passwordInput = await screen.findByPlaceholderText('비밀번호');
      fireEvent.change(passwordInput, { target: { value: validPassword } });

      const passwordConfirmationInput = await screen.findByPlaceholderText('비밀번호 확인');
      fireEvent.change(passwordConfirmationInput, { target: { value: validPassword } });

      const signUpButton = await (await screen.findAllByText('회원가입')).find(button => button.tagName === 'BUTTON');
      fireEvent.click(signUpButton!);

      waitFor(() => {
        expect(screen.getByText(SNACKBAR_MESSAGE.SUCCESS_SIGN_UP)).toBeInTheDocument();
      });
    });
  });
});
