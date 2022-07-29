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

  async function registerEmail(email: string) {
    const emailInput = await screen.findByPlaceholderText('이메일');

    fireEvent.change(emailInput, { target: { value: email } });
    fireEvent.click(await screen.findByText('인증번호 받기'));
  }

  async function registerVerificationCode(code: string) {
    const verificationCodeInput = await screen.findByPlaceholderText('인증번호');

    fireEvent.change(verificationCodeInput, { target: { value: code } });
    fireEvent.click(await screen.findByText('확인'));
  }

  async function registerID(ID: string) {
    const IDInput = await screen.findByPlaceholderText('아이디');
    const [IDsubmitButton] = await screen.findAllByText('중복 확인');

    fireEvent.change(IDInput, { target: { value: ID } });
    fireEvent.click(IDsubmitButton);
    await waitFor(() => {});
  }

  async function registerNickname(nickname: string) {
    const [, nicknameSubmitButton] = await screen.findAllByText('중복 확인');
    const nickNameInput = await screen.findByPlaceholderText('닉네임');

    fireEvent.change(nickNameInput, { target: { value: nickname } });
    fireEvent.click(nicknameSubmitButton);
  }

  async function registerPassword(password: string) {
    const passwordInput = await screen.findByPlaceholderText('비밀번호');
    fireEvent.change(passwordInput, { target: { value: password } });
  }

  async function registerPasswordConfirmation(password: string) {
    const passwordConfirmationInput = await screen.findByPlaceholderText('비밀번호 확인');
    fireEvent.change(passwordConfirmationInput, { target: { value: password } });
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

    test('올바른 이메일과 비밀번호를 입력할 경우 로그인에 성공한다.', async () => {
      attemptLogin(memberList[0].username, memberList[0].password);

      await waitFor(async () => {
        expect(await screen.findByText(SNACKBAR_MESSAGE.SUCCESS_LOGIN)).toBeInTheDocument();
      });
    });

    test('올바르지 않은 이메일과 비밀번호를 입력할 경우 로그인에 실패한다.', async () => {
      attemptLogin('InvalidID', memberList[0].password);

      await waitFor(async () => {
        expect(await screen.findByText(SNACKBAR_MESSAGE.FAIL_LOGIN)).toBeInTheDocument();
      });
    });
  });

  describe('회원가입 테스트', () => {
    beforeEach(async () => {
      fireEvent.click(await screen.findByText('로그인'));
      fireEvent.click(await screen.findByText('간편 회원가입하기'));
    });

    const validMemberForm = validMemberEmail.find(member => !member.isSignedUp)!;
    const existedUser = validMemberEmail.find(member => member.isSignedUp)!;

    test('올바른 양식을 입력하면 회원가입에 성공한다.', async () => {
      registerEmail(validMemberForm.email);

      await waitFor(async () => {
        await registerVerificationCode(validMemberForm.code);
        await registerID('validID');
        await registerNickname('validNickname');
      });
      await screen.findAllByText('중복 확인 완료');

      const validPassword = 'validPassword123!';
      registerPassword(validPassword);
      registerPasswordConfirmation(validPassword);

      await waitFor(async () => {
        const signUpButton = (await screen.findAllByText('회원가입')).find(button => button.tagName === 'BUTTON');
        fireEvent.click(signUpButton!);
      });

      await waitFor(async () => {
        expect(await screen.findByText(SNACKBAR_MESSAGE.SUCCESS_SIGN_UP)).toBeInTheDocument();
      });
    });

    test('우아한 테크코스 크루가 아닐경우 해당 에러 메시지를 표시한다.', async () => {
      registerEmail('notIncludedEmail@gmail.com');

      await waitFor(() => {
        expect(screen.getByText('우아한테크코스 크루가 아닙니다.')).toBeInTheDocument();
      });
    });

    test('이미 가입된 크루일 경우 해당 에러 메시지를 표시한다.', async () => {
      registerEmail(existedUser?.email!);

      await waitFor(() => {
        expect(screen.getByText('이미 가입된 크루입니다.')).toBeInTheDocument();
      });
    });

    test('인증번호가 올바르지 않을 경우 해당 에러 메시지를 표시한다.', async () => {
      registerEmail(validMemberForm.email);
      registerVerificationCode('IncorrectCode');

      await waitFor(() => {
        expect(screen.getByText('잘못된 인증번호입니다.')).toBeInTheDocument();
      });
    });

    test('이미 존재하는 아이디를 입력한 경우 해당 에러 메시지를 표시한다.', async () => {
      registerID(existedUser?.ID!);

      await waitFor(() => {
        expect(screen.getByText('중복된 아이디입니다.')).toBeInTheDocument();
      });
    });

    test('이미 존재하는 닉네임을 입력한 경우 해당 에러 메시지를 표시한다.', async () => {
      registerNickname(existedUser?.nickname!);

      await waitFor(() => {
        expect(screen.getByText('중복된 닉네임입니다.')).toBeInTheDocument();
      });
    });
  });
});
