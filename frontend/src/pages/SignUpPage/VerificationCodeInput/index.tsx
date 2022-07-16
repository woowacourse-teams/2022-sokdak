import React, { Dispatch, SetStateAction } from 'react';

import InputBox from '@/components/@shared/InputBox';
import { useInput } from '@/components/@shared/InputBox/useInput';

import useVerificationCodeCheck from '@/hooks/queries/member/useVerificationCodeCheck';

import * as Styled from '../index.styles';

interface VerificationCodeInput extends ReturnType<typeof useInput> {
  isAnimationActive: boolean;
  setIsAnimationActive: Dispatch<SetStateAction<boolean>>;
  email: string;
}

const VerificationCodeInput = ({
  email,
  value,
  setValue,
  error,
  setError,
  isAnimationActive,
  setIsAnimationActive,
}: VerificationCodeInput) => {
  const { mutate } = useVerificationCodeCheck({
    onSuccess: () => {},
    onError(error) {
      setError(error.response?.data.message!);
      setIsAnimationActive(true);
    },
  });

  const handleEmailFormSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    mutate({ email, code: value });
  };

  return (
    <InputBox value={value} setValue={setValue} error={error} setError={setError}>
      <Styled.InputForm onSubmit={handleEmailFormSubmit}>
        <InputBox.Input
          handleInvalid={() => {
            setError('');
          }}
          placeholder="인증번호"
          isAnimationActive={isAnimationActive}
          setIsAnimationActive={setIsAnimationActive}
          required
        />
        <InputBox.SubmitButton disabled={error !== '' || value === ''}>확인</InputBox.SubmitButton>
      </Styled.InputForm>
      <InputBox.ErrorMessage />
    </InputBox>
  );
};

export default VerificationCodeInput;
