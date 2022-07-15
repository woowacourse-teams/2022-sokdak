import { useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import IDInput from './components/IDInput';
import PasswordInput from './components/PasswordInput';
import Layout from '@/components/@styled/Layout';

import AuthContext from '@/context/Auth';
import SnackbarContext from '@/context/Snackbar';

import useLogin from '@/hooks/queries/member/useLogin';

import * as Styled from './index.styles';

import PATH from '@/constants/path';

const LoginPage = () => {
  const [ID, setID] = useState('');
  const [IDError, setIDError] = useState('');
  const [password, setPassword] = useState('');
  const [passwordError, setPasswordError] = useState('');
  const { showSnackbar } = useContext(SnackbarContext);
  const navigate = useNavigate();
  const { setIsLogin, setUserName } = useContext(AuthContext);

  const { mutate } = useLogin({
    onSuccess: () => {
      showSnackbar('로그인에 성공하였습니다');
      setIsLogin(true);
      setUserName(ID);
      navigate(PATH.HOME);
    },
    onError: () => {
      setIDError(' ');
      setPasswordError(' ');
      showSnackbar('아이디와 비밀번호를 확인해주세요');
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
        <IDInput value={ID} setValue={setID} error={IDError} setError={setIDError} />
        <PasswordInput value={password} setValue={setPassword} error={passwordError} setError={setPasswordError} />
        <Styled.SubmitButton>로그인</Styled.SubmitButton>
        <Styled.SignUpText>
          속닥속닥, <Styled.SignUpLink to={PATH.SIGN_UP}>간편 회원가입하기</Styled.SignUpLink>
        </Styled.SignUpText>
      </Styled.LoginForm>
    </Layout>
  );
};

export default LoginPage;
