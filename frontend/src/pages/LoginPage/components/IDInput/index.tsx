import InputBox from '@/components/@shared/InputBox';
import { useInput } from '@/components/@shared/InputBox/useInput';

const IDInput = ({ value, setValue, error, setError }: ReturnType<typeof useInput>) => {
  return (
    <InputBox value={value} setValue={setValue} error={error} setError={setError}>
      <InputBox.Input
        type="text"
        placeholder="아이디"
        handleInvalid={() => {
          setError('아이디를 입력해주세요.');
        }}
        required
      />
      <InputBox.ErrorMessage />
    </InputBox>
  );
};

export default IDInput;
