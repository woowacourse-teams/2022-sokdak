import { useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import EmailInput from './components/EmailInput';
import IDInput from './components/IDInput';
import NicknameInput from './components/NicknameInput';
import PasswordConfirmationInput from './components/PasswordConfirmationInput';
import PasswordInput from './components/PasswordInput';
import VerificationCodeInput from './components/VerificationCodeInput';
import { useInput } from '@/components/@shared/InputBox/useInput';
import Layout from '@/components/@styled/Layout';

import SnackbarContext from '@/context/Snackbar';

import useSignUp from '@/hooks/queries/member/useSignUp';

import * as Styled from './index.styles';

import PATH from '@/constants/path';
import SNACKBAR_MESSAGE from '@/constants/snackbar';

const SignUpPage = () => {
  const { value: email, setValue: setEmail, error: emailError, setError: setEmailError } = useInput();
  const {
    value: verificationCode,
    setValue: setVerificationCode,
    error: verificationCodeError,
    setError: setVerificationCodeError,
  } = useInput();
  const { value: ID, setValue: setID, error: IDError, setError: setIDError } = useInput();
  const { value: password, setValue: setPassword, error: passwordError, setError: setPasswordError } = useInput();
  const { value: nickname, setValue: setNickname, error: nicknameError, setError: setNicknameError } = useInput();
  const {
    value: passwordConfirmation,
    setValue: setPasswordConfirmation,
    error: passwordConfirmationError,
    setError: setPasswordConfirmationError,
  } = useInput();

  const [isLoginAnimationActive, setIsLoginAnimationActive] = useState(false);
  const [isPasswordAnimationActive, setIsPasswordAnimationActive] = useState(false);
  const [isVerificationCodeAnimationActive, setIsVerificationCodeAnimationActive] = useState(false);
  const [isIDAnimationActive, setIsIDAnimationActive] = useState(false);
  const [isNicknameAnimationActive, setIsNicknameAnimationActive] = useState(false);
  const [isPasswordConfirmationAnimationActive, setIsPasswordConfirmationAnimationActive] = useState(false);

  const [isEmailSet, setIsEmailSet] = useState(false);
  const [isVerificationCodeSet, setIsVerificationCodeSet] = useState(false);
  const [isIDSet, setIDSet] = useState(false);
  const [isNicknameSet, setIsNicknameSet] = useState(false);

  const { showSnackbar } = useContext(SnackbarContext);
  const navigate = useNavigate();

  const { mutate } = useSignUp({
    onSuccess: () => {
      showSnackbar('회원가입에 성공하였습니다.');
      navigate(PATH.LOGIN);
    },
    onError: () => {
      setEmailError(' ');
      setPasswordError(' ');
      setIsLoginAnimationActive(true);
      setIsPasswordAnimationActive(true);
      showSnackbar(SNACKBAR_MESSAGE.FAIL_LOGIN);
    },
  });

  const handleSubmitButton = (e: React.FormEvent) => {
    e.preventDefault();
    setEmailError('');
    setPasswordError('');
    mutate({ username: ID, password, code: verificationCode, email, nickname, passwordConfirmation });
  };

  return (
    <Layout>
      <Styled.SignUpForm>
        <Styled.Heading>회원가입</Styled.Heading>
        <EmailInput
          value={email}
          setValue={setEmail}
          error={emailError}
          setError={setEmailError}
          isAnimationActive={isLoginAnimationActive}
          setIsAnimationActive={setIsLoginAnimationActive}
          isSet={isEmailSet}
          setIsSet={setIsEmailSet}
          isVerified={isVerificationCodeSet}
        />
        <VerificationCodeInput
          value={verificationCode}
          setValue={setVerificationCode}
          error={verificationCodeError}
          setError={setVerificationCodeError}
          isAnimationActive={isVerificationCodeAnimationActive}
          setIsAnimationActive={setIsVerificationCodeAnimationActive}
          email={email}
          setIsVerified={setIsVerificationCodeSet}
          isEmailSet={isEmailSet}
          isVerified={isVerificationCodeSet}
        />

        <IDInput
          isAnimationActive={isIDAnimationActive}
          setIsAnimationActive={setIsIDAnimationActive}
          value={ID}
          setValue={setID}
          error={IDError}
          setError={setIDError}
          isSet={isIDSet}
          setIsSet={setIDSet}
        />
        <NicknameInput
          isAnimationActive={isNicknameAnimationActive}
          setIsAnimationActive={setIsNicknameAnimationActive}
          value={nickname}
          setValue={setNickname}
          error={nicknameError}
          setError={setNicknameError}
          isSet={isNicknameSet}
          setIsSet={setIsNicknameSet}
        />
        <PasswordInput
          isAnimationActive={isPasswordAnimationActive}
          setIsAnimationActive={setIsPasswordAnimationActive}
          value={password}
          setValue={setPassword}
          error={passwordError}
          setError={setPasswordError}
        />
        <PasswordConfirmationInput
          isAnimationActive={isPasswordConfirmationAnimationActive}
          setIsAnimationActive={setIsPasswordConfirmationAnimationActive}
          value={passwordConfirmation}
          setValue={setPasswordConfirmation}
          error={passwordConfirmationError}
          setError={setPasswordConfirmationError}
          password={password}
        />
        <Styled.SubmitButton onClick={handleSubmitButton} disabled={!isEmailSet || !isIDSet || !isNicknameSet}>
          회원가입
        </Styled.SubmitButton>
        <Styled.SignUpText>
          이미 회원이신가요? <Styled.LoginLink to={PATH.LOGIN}>로그인하기</Styled.LoginLink>
        </Styled.SignUpText>
      </Styled.SignUpForm>
    </Layout>
  );
};

export default SignUpPage;
