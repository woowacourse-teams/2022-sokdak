import { Dispatch, SetStateAction } from 'react';

import InputBox from '@/components/@shared/InputBox';
import { useInput } from '@/components/@shared/InputBox/useInput';

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
  return (
    <InputBox
      value={value}
      setValue={setValue}
      error={error}
      setError={setError}
      isAnimationActive={isAnimationActive}
      setIsAnimationActive={setIsAnimationActive}
    >
      <InputBox.Input
        type="password"
        placeholder="비밀번호"
        handleInvalid={() => {
          setError('비밀번호를 입력해주세요.');
        }}
        required
      />
      <InputBox.ErrorMessage />
    </InputBox>
  );
};

export default PasswordInput;
