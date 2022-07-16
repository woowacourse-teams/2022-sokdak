import { Dispatch, SetStateAction } from 'react';

import InputBox from '@/components/@shared/InputBox';
import { useInput } from '@/components/@shared/InputBox/useInput';

interface IDInputProps extends ReturnType<typeof useInput> {
  isAnimationActive: boolean;
  setIsAnimationActive: Dispatch<SetStateAction<boolean>>;
}

const IDInput = ({ value, setValue, error, setError, isAnimationActive, setIsAnimationActive }: IDInputProps) => {
  return (
    <InputBox value={value} setValue={setValue} error={error} setError={setError}>
      <InputBox.Input
        type="text"
        placeholder="아이디"
        handleInvalid={() => {
          setError('아이디를 입력해주세요.');
        }}
        required
        isAnimationActive={isAnimationActive}
        setIsAnimationActive={setIsAnimationActive}
      />
      <InputBox.ErrorMessage />
    </InputBox>
  );
};

export default IDInput;
