import { Dispatch, SetStateAction } from 'react';

import InputBox from '@/components/@shared/InputBox';
import { useInput } from '@/components/@shared/InputBox/useInput';

import { MEMBER, SIGN_UP_ERROR } from '@/constants/signUp';
import { isValidPassword } from '@/utils/regExp';

import * as Styled from '../../index.styles';

interface PasswordInputProps extends ReturnType<typeof useInput> {
  isAnimationActive: boolean;
  setIsAnimationActive: Dispatch<SetStateAction<boolean>>;
}

const PasswordInput = ({
  value,
  setValue,
  error,
  setError,
  isAnimationActive,
  setIsAnimationActive,
}: PasswordInputProps) => {
  const handleBlur = () => {
    if (!isValidPassword(value)) {
      setError(SIGN_UP_ERROR.INVALID_PASSWORD);
    }
  };
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
          placeholder="비밀번호"
          onBlur={handleBlur}
          required
        />
        <div />
        {error ? (
          <InputBox.ErrorMessage />
        ) : (
          <Styled.MessageContainer>
            <Styled.Message>{`영어,숫자,특수문자가 포함된 ${MEMBER.LIMIT.PASSWORD.MINIMUM_LENGTH}~${MEMBER.LIMIT.PASSWORD.MAXIMUM_LENGTH}자`}</Styled.Message>
          </Styled.MessageContainer>
        )}
      </Styled.PasswordInputContainer>
    </InputBox>
  );
};

export default PasswordInput;
