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
  const form = {
    email: useInput(),
    verificationCode: useInput(),
    ID: useInput(),
    nickname: useInput(),
    password: useInput(),
    passwordConfirmation: useInput(),
  };

  const [isEmailSet, setIsEmailSet] = useState(false);
  const [isVerificationCodeSet, setIsVerificationCodeSet] = useState(false);
  const [isIDSet, setIDSet] = useState(false);
  const [isNicknameSet, setIsNicknameSet] = useState(false);

  const { showSnackbar } = useContext(SnackbarContext);

  const navigate = useNavigate();

  const { mutate } = useSignUp({
    onSuccess: () => {
      showSnackbar(SNACKBAR_MESSAGE.SUCCESS_SIGN_UP);
      navigate(PATH.LOGIN);
    },
    onError: error => {
      Object.values(form).forEach(item => {
        item.setIsAnimationActive(true);
      });

      showSnackbar(error.response?.data.message!);
    },
  });

  const handleSubmitButton = (e: React.FormEvent) => {
    e.preventDefault();
    form.email.setError('');
    form.password.setError('');

    mutate({
      username: form.ID.value,
      password: form.password.value,
      code: form.verificationCode.value,
      email: form.email.value,
      nickname: form.nickname.value,
      passwordConfirmation: form.passwordConfirmation.value,
    });
  };

  return (
    <Layout>
      <Styled.SignUpForm>
        <Styled.Heading>회원가입</Styled.Heading>
        <EmailInput {...form.email} isSet={isEmailSet} setIsSet={setIsEmailSet} isVerified={isVerificationCodeSet} />
        <VerificationCodeInput
          {...form.verificationCode}
          email={form.email.value}
          setIsVerified={setIsVerificationCodeSet}
          isEmailSet={isEmailSet}
          isVerified={isVerificationCodeSet}
        />
        <IDInput {...form.ID} isSet={isIDSet} setIsSet={setIDSet} />
        <NicknameInput {...form.nickname} isSet={isNicknameSet} setIsSet={setIsNicknameSet} />
        <PasswordInput {...form.password} />
        <PasswordConfirmationInput {...form.passwordConfirmation} password={form.password.value} />
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
