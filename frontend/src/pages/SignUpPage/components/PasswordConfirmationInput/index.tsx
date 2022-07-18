import { Dispatch, SetStateAction, useLayoutEffect } from 'react';

import InputBox from '@/components/@shared/InputBox';
import { useInput } from '@/components/@shared/InputBox/useInput';

import { isValidPassword } from '@/utils/regExp';

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
  const handleBlur = () => {
    if (!isValidPassword(value)) {
      setError('비밀번호는 영어,숫자,특수문자가 포함된 8~20자입니다.');
    }
  };

  useLayoutEffect(() => {
    if (!value) return;
    if (value !== password) {
      setError('비밀번호와 일치하지 않습니다');
    }

    if (value === password) {
      setError('');
    }
  }, [value]);

  return (
    <InputBox value={value} setValue={setValue} error={error} setError={setError}>
      <Styled.InputForm as="div">
        <div>
          <InputBox.Input
            type="password"
            handleInvalid={() => {
              setError('비밀번호를 입력해주세요');
            }}
            placeholder="비밀번호 확인"
            isAnimationActive={isAnimationActive}
            setIsAnimationActive={setIsAnimationActive}
            onBlur={handleBlur}
            required
          />
          <InputBox.ErrorMessage />
        </div>
      </Styled.InputForm>
    </InputBox>
  );
};

export default PasswordConfirmationInput;
