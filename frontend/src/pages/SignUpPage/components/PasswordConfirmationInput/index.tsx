import { Dispatch, SetStateAction, useLayoutEffect } from 'react';

import InputBox from '@/components/@shared/InputBox';
import { useInput } from '@/components/@shared/InputBox/useInput';

import { SIGN_UP_ERROR } from '@/constants/signUp';

import * as Styled from '../../index.styles';

interface PasswordConfirmationInputProps extends ReturnType<typeof useInput> {
  isAnimationActive: boolean;
  setIsAnimationActive: Dispatch<SetStateAction<boolean>>;
  password: string;
}

const PasswordConfirmationInput = ({
  value,
  setValue,
  error,
  setError,
  isAnimationActive,
  setIsAnimationActive,
  password,
}: PasswordConfirmationInputProps) => {
  useLayoutEffect(() => {
    if (!value) return;
    if (value !== password) {
      return setError(SIGN_UP_ERROR.INVALID_PASSWORD_CONFIRMATION);
    }

    if (value === password) {
      return setError('');
    }
  }, [value]);

  return (
    <InputBox
      value={value}
      setValue={setValue}
      error={error}
      setError={setError}
      isAnimationActive={isAnimationActive}
      setIsAnimationActive={setIsAnimationActive}
    >
      <Styled.PasswordInputContainer>
        <InputBox.Input
          type="password"
          handleInvalid={() => {
            setError(SIGN_UP_ERROR.BLANK_PASSWORD);
          }}
          placeholder="비밀번호 확인"
          required
        />
        <div />
        <InputBox.ErrorMessage />
      </Styled.PasswordInputContainer>
    </InputBox>
  );
};

export default PasswordConfirmationInput;
