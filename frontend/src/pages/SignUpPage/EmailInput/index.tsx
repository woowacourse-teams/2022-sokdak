import React, { Dispatch, SetStateAction } from 'react';

import InputBox from '@/components/@shared/InputBox';
import { useInput } from '@/components/@shared/InputBox/useInput';

import useEmailCheck from '@/hooks/queries/member/useEmailCheck';

import { isValidEmail } from '@/utils/regExp';

import * as Styled from '../index.styles';

interface EmailInputProps extends ReturnType<typeof useInput> {
  isAnimationActive: boolean;
  setIsAnimationActive: Dispatch<SetStateAction<boolean>>;
}

const EmailInput = ({ value, setValue, error, setError, isAnimationActive, setIsAnimationActive }: EmailInputProps) => {
  const { mutate } = useEmailCheck({
    onSuccess: () => {},
    onError(error) {
      setError(error.response?.data.message!);
      setIsAnimationActive(true);
    },
  });

  const handleChangeEmailInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (!isValidEmail(value)) {
      setError('올바른 이메일 형식을 입력해주세요');
    }
    if (isValidEmail(value)) {
      setError('');
    }
  };

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
          onChange={handleChangeEmailInput}
          onKeyDown={handleKeyDown}
          isAnimationActive={isAnimationActive}
          setIsAnimationActive={setIsAnimationActive}
          required
        />
        <InputBox.SubmitButton disabled={error !== '' || value === ''}>인증번호 받기</InputBox.SubmitButton>
      </Styled.InputForm>
      <InputBox.ErrorMessage />
    </InputBox>
  );
};

export default EmailInput;
