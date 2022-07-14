import { useState } from 'react';

import IDInput from './components/IDInput';
import PasswordInput from './components/PasswordInput';
import Layout from '@/components/@styled/Layout';

import * as Styled from './index.styles';

import PATH from '@/constants/path';

const LoginPage = () => {
  const [ID, setID] = useState('');
  const [IDError, setIDError] = useState('');
  const [password, setPassword] = useState('');
  const [passwordError, setPasswordError] = useState('');
  return (
    <Layout>
      <Styled.LoginForm>
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
