import { Dispatch, SetStateAction } from 'react';

import InputBox from '@/components/@shared/InputBox';
import { useInput } from '@/components/@shared/InputBox/useInput';

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
      setError('비밀번호는 영어,숫자,특수문자가 포함되어 8자에서 20자 이하입니다.');
    }
  };
  return (
    <InputBox value={value} setValue={setValue} error={error} setError={setError}>
      <Styled.InputForm as="div">
        <div>
          <InputBox.Input
            type="password"
            handleInvalid={() => {
              setError('비밀번호를 입력해주세요');
            }}
            placeholder="비밀번호"
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

export default PasswordInput;
