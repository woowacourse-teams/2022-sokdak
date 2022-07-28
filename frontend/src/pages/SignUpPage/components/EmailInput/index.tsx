import React, { Dispatch, SetStateAction, useLayoutEffect } from 'react';

import InputBox from '@/components/@shared/InputBox';
import { useInput } from '@/components/@shared/InputBox/useInput';

import useEmailCheck from '@/hooks/queries/member/useEmailCheck';

import { SIGN_UP_ERROR } from '@/constants/signUp';
import { isValidEmail } from '@/utils/regExp';

import * as Styled from '../../index.styles';

interface EmailInputProps extends ReturnType<typeof useInput> {
  isAnimationActive: boolean;
  setIsAnimationActive: Dispatch<SetStateAction<boolean>>;
  isSet: boolean;
  setIsSet: Dispatch<SetStateAction<boolean>>;
  isVerified: boolean;
}

const EmailInput = ({
  value,
  setValue,
  error,
  setError,
  isAnimationActive,
  setIsAnimationActive,
  isSet,
  setIsSet,
  isVerified,
}: EmailInputProps) => {
  const { isLoading, mutate } = useEmailCheck({
    onSuccess: () => {
      setIsSet(true);
    },
    onError(error) {
      setError(error.response?.data.message!);
      setIsAnimationActive(true);
    },
  });
  useLayoutEffect(() => {
    if (!value) {
      return;
    }
    if (!isValidEmail(value)) {
      setError(SIGN_UP_ERROR.INVALID_EMAIL);
    }
    if (isValidEmail(value)) {
      setError('');
    }
  }, [value]);

  const handleEmailFormSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    mutate({ email: value });
  };

  const handleKeyDown = () => {};
  return (
    <InputBox
      value={value}
      setValue={setValue}
      error={error}
      setError={setError}
      isAnimationActive={isAnimationActive}
      setIsAnimationActive={setIsAnimationActive}
    >
      <Styled.InputForm onSubmit={handleEmailFormSubmit}>
        <InputBox.Input
          handleInvalid={() => {
            setError(SIGN_UP_ERROR.INVALID_EMAIL);
          }}
          type="email"
          placeholder="이메일"
          onKeyDown={handleKeyDown}
          disabled={isVerified || isSet}
          required
        />
        {isLoading ? (
          <InputBox.SubmitButton disabled={true}>로딩중</InputBox.SubmitButton>
        ) : (
          <InputBox.SubmitButton disabled={error !== '' || value === '' || isSet}>
            {isSet ? (isVerified ? '인증 완료' : '인증 중') : '인증번호 받기'}
          </InputBox.SubmitButton>
        )}
      </Styled.InputForm>
      <InputBox.ErrorMessage />
    </InputBox>
  );
};

export default EmailInput;
