import React, { Dispatch, SetStateAction, useLayoutEffect } from 'react';

import InputBox from '@/components/@shared/InputBox';
import { useInput } from '@/components/@shared/InputBox/useInput';

import useEmailCheck from '@/hooks/queries/member/useEmailCheck';

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
  const { mutate } = useEmailCheck({
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
      setError('올바른 이메일 형식을 입력해주세요');
    }
    if (isValidEmail(value)) {
      setError('');
    }
  }, [value]);

  const handleEmailFormSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    mutate({ email: value });
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {};
  return (
    <InputBox value={value} setValue={setValue} error={error} setError={setError}>
      <Styled.InputForm onSubmit={handleEmailFormSubmit}>
        <InputBox.Input
          handleInvalid={() => {
            setError('올바른 이메일 형식을 입력해주세요');
          }}
          type="email"
          placeholder="이메일"
          onKeyDown={handleKeyDown}
          isAnimationActive={isAnimationActive}
          setIsAnimationActive={setIsAnimationActive}
          disabled={isVerified || isSet}
          required
        />
        <InputBox.SubmitButton disabled={error !== '' || value === '' || isSet}>
          {isSet ? (isVerified ? '인증 완료' : '인증 중') : '인증번호 받기'}
        </InputBox.SubmitButton>
      </Styled.InputForm>
      <InputBox.ErrorMessage />
    </InputBox>
  );
};

export default EmailInput;
