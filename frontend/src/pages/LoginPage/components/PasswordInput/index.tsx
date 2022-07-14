import InputBox from '@/components/@shared/InputBox';
import { useInput } from '@/components/@shared/InputBox/useInput';

const PasswordInput = ({ value, setValue, error, setError }: ReturnType<typeof useInput>) => {
  return (
    <InputBox value={value} setValue={setValue} error={error} setError={setError}>
      <InputBox.Input
        type="password"
        placeholder="비밀번호"
        handleInvalid={() => {
          setError('정확한 비밀번호 형식을 입력해주세요');
        }}
        required
      />
      <InputBox.ErrorMessage />
    </InputBox>
  );
};

export default PasswordInput;
