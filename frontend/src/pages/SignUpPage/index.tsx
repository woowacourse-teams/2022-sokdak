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
import CheckBox from '@/components/CheckBox';

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
  const [isCourseCrew, setIsCourseCrew] = useState(true);

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
      code: isCourseCrew ? form.verificationCode.value : null,
      email: isCourseCrew ? form.email.value : null,
      nickname: form.nickname.value,
      passwordConfirmation: form.passwordConfirmation.value,
    });
  };

  return (
    <Layout>
      <Styled.SignUpForm>
        <Styled.Heading>회원가입</Styled.Heading>
        <CheckBox isChecked={isCourseCrew} labelText={'우아한테크코스 크루입니까?'} setIsChecked={setIsCourseCrew} />
        {isCourseCrew && (
          <>
            <EmailInput
              {...form.email}
              isSet={isEmailSet}
              setIsSet={setIsEmailSet}
              isVerified={isVerificationCodeSet}
              isCourseCrew={isCourseCrew}
            />
            <VerificationCodeInput
              {...form.verificationCode}
              email={form.email.value}
              setIsVerified={setIsVerificationCodeSet}
              isEmailSet={isEmailSet}
              isVerified={isVerificationCodeSet}
            />
          </>
        )}
        <IDInput {...form.ID} isSet={isIDSet} setIsSet={setIDSet} />
        <NicknameInput {...form.nickname} isSet={isNicknameSet} setIsSet={setIsNicknameSet} />
        <PasswordInput {...form.password} />
        <PasswordConfirmationInput {...form.passwordConfirmation} password={form.password.value} />
        <Styled.SubmitButton
          onClick={handleSubmitButton}
          disabled={!(!isCourseCrew || isEmailSet) || !isIDSet || !isNicknameSet}
        >
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
