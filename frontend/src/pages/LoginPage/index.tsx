import { useContext } from 'react';
import { useNavigate } from 'react-router-dom';

import IDInput from './components/IDInput';
import PasswordInput from './components/PasswordInput';
import { useInput } from '@/components/@shared/InputBox/useInput';
import Layout from '@/components/@styled/Layout';

import AuthContext from '@/context/Auth';
import SnackbarContext from '@/context/Snackbar';

import useLogin from '@/hooks/queries/member/useLogin';

import * as Styled from './index.styles';

import PATH from '@/constants/path';
import SNACKBAR_MESSAGE from '@/constants/snackbar';

const LoginPage = () => {
  const form = {
    ID: useInput(),
    password: useInput(),
  };

  const { showSnackbar } = useContext(SnackbarContext);
  const navigate = useNavigate();
  const { setIsLogin, setUserName } = useContext(AuthContext);

  const { mutate } = useLogin({
    onSuccess: () => {
      showSnackbar(SNACKBAR_MESSAGE.SUCCESS_LOGIN);
      setIsLogin(true);
      setUserName(form.ID.value);
      navigate(PATH.HOME);
    },
    onError: () => {
      Object.values(form).forEach(({ setError, setIsAnimationActive }) => {
        setError(' ');
        setIsAnimationActive(true);
      });
      showSnackbar(SNACKBAR_MESSAGE.FAIL_LOGIN);
    },
  });

  const handleSubmitButton = (e: React.FormEvent) => {
    e.preventDefault();
    Object.values(form).forEach(({ setError }) => {
      setError('');
    });
    mutate({ username: form.ID.value, password: form.password.value });
  };

  return (
    <Layout>
      <Styled.LoginForm onSubmit={handleSubmitButton}>
        <Styled.Heading>로그인</Styled.Heading>
        <IDInput {...form.ID} />
        <PasswordInput {...form.password} />
        <Styled.SubmitButton>로그인</Styled.SubmitButton>
        <Styled.SignUpText>
          속닥속닥, <Styled.SignUpLink to={PATH.SIGN_UP}>간편 회원가입하기</Styled.SignUpLink>
        </Styled.SignUpText>
      </Styled.LoginForm>
    </Layout>
  );
};

export default LoginPage;
