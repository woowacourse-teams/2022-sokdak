import { useContext } from 'react';
import { useNavigate } from 'react-router-dom';

import IDInput from './components/IDInput';
import PasswordInput from './components/PasswordInput';
import { useInput } from '@/components/@shared/InputBox/useInput';
import Layout from '@/components/@styled/Layout';

import AuthContext from '@/context/Auth';

import useLogin from '@/hooks/queries/member/useLogin';
import useSnackbar from '@/hooks/useSnackbar';

import * as Styled from './index.styles';

import PATH from '@/constants/path';
import SNACKBAR_MESSAGE from '@/constants/snackbar';

const LoginPage = () => {
  const {
    value: ID,
    setValue: setID,
    error: IDError,
    setError: setIDError,
    isAnimationActive: isLoginAnimationActive,
    setIsAnimationActive: setIsLoginAnimationActive,
  } = useInput();
  const {
    value: password,
    setValue: setPassword,
    error: passwordError,
    setError: setPasswordError,
    isAnimationActive: isPasswordAnimationActive,
    setIsAnimationActive: setIsPasswordAnimationActive,
  } = useInput();

  const { showSnackbar } = useSnackbar();
  const navigate = useNavigate();
  const { setIsLogin, setUserName } = useContext(AuthContext);

  const { mutate } = useLogin({
    onSuccess: () => {
      showSnackbar(SNACKBAR_MESSAGE.SUCCESS_LOGIN);
      setIsLogin(true);
      setUserName(ID);
      navigate(PATH.HOME);
    },
    onError: () => {
      setIDError(' ');
      setPasswordError(' ');
      setIsLoginAnimationActive(true);
      setIsPasswordAnimationActive(true);
      showSnackbar(SNACKBAR_MESSAGE.FAIL_LOGIN);
    },
  });

  const handleSubmitButton = (e: React.FormEvent) => {
    e.preventDefault();
    setIDError('');
    setPasswordError('');
    mutate({ username: ID, password });
  };

  return (
    <Layout>
      <Styled.LoginForm onSubmit={handleSubmitButton}>
        <Styled.Heading>로그인</Styled.Heading>
        <IDInput
          value={ID}
          setValue={setID}
          error={IDError}
          setError={setIDError}
          isAnimationActive={isLoginAnimationActive}
          setIsAnimationActive={setIsLoginAnimationActive}
        />
        <PasswordInput
          value={password}
          setValue={setPassword}
          error={passwordError}
          setError={setPasswordError}
          isAnimationActive={isPasswordAnimationActive}
          setIsAnimationActive={setIsPasswordAnimationActive}
        />
        <Styled.SubmitButton>로그인</Styled.SubmitButton>
        <Styled.SignUpText>
          속닥속닥, <Styled.SignUpLink to={PATH.SIGN_UP}>간편 회원가입하기</Styled.SignUpLink>
        </Styled.SignUpText>
      </Styled.LoginForm>
    </Layout>
  );
};

export default LoginPage;
