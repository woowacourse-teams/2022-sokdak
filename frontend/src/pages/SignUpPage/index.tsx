import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import EmailInput from './components/EmailInput';
import IDInput from './components/IDInput';
import NicknameInput from './components/NicknameInput';
import PasswordConfirmationInput from './components/PasswordConfirmationInput';
import PasswordInput from './components/PasswordInput';
import VerificationCodeInput from './components/VerificationCodeInput';
import { useInput } from '@/components/@shared/InputBox/useInput';
import Layout from '@/components/@styled/Layout';

import useSignUp from '@/hooks/queries/member/useSignUp';
import useSnackbar from '@/hooks/useSnackbar';

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

  const { showSnackbar } = useSnackbar();

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
        <EmailInput
          value={form.email.value}
          setValue={form.email.setValue}
          error={form.email.error}
          setError={form.email.setError}
          isAnimationActive={form.email.isAnimationActive}
          setIsAnimationActive={form.email.setIsAnimationActive}
          isSet={isEmailSet}
          setIsSet={setIsEmailSet}
          isVerified={isVerificationCodeSet}
        />
        <VerificationCodeInput
          value={form.verificationCode.value}
          setValue={form.verificationCode.setValue}
          error={form.verificationCode.error}
          setError={form.verificationCode.setError}
          isAnimationActive={form.verificationCode.isAnimationActive}
          setIsAnimationActive={form.verificationCode.setIsAnimationActive}
          email={form.email.value}
          setIsVerified={setIsVerificationCodeSet}
          isEmailSet={isEmailSet}
          isVerified={isVerificationCodeSet}
        />

        <IDInput
          value={form.ID.value}
          setValue={form.ID.setValue}
          error={form.ID.error}
          setError={form.ID.setError}
          isAnimationActive={form.ID.isAnimationActive}
          setIsAnimationActive={form.ID.setIsAnimationActive}
          isSet={isIDSet}
          setIsSet={setIDSet}
        />
        <NicknameInput
          value={form.nickname.value}
          setValue={form.nickname.setValue}
          error={form.nickname.error}
          setError={form.nickname.setError}
          isAnimationActive={form.nickname.isAnimationActive}
          setIsAnimationActive={form.nickname.setIsAnimationActive}
          isSet={isNicknameSet}
          setIsSet={setIsNicknameSet}
        />
        <PasswordInput
          value={form.password.value}
          setValue={form.password.setValue}
          error={form.password.error}
          setError={form.password.setError}
          isAnimationActive={form.password.isAnimationActive}
          setIsAnimationActive={form.password.setIsAnimationActive}
        />
        <PasswordConfirmationInput
          value={form.passwordConfirmation.value}
          setValue={form.passwordConfirmation.setValue}
          error={form.passwordConfirmation.error}
          setError={form.passwordConfirmation.setError}
          isAnimationActive={form.passwordConfirmation.isAnimationActive}
          setIsAnimationActive={form.passwordConfirmation.setIsAnimationActive}
          password={form.password.value}
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
