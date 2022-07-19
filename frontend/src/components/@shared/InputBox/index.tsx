import { Dispatch, SetStateAction } from 'react';

import ErrorMessage from './components/ErrorMessage';
import Input from './components/Input';
import SubmitButton from './components/SubmitButton';

import { InputContextProvider } from './useInputContext';

interface InputBoxProps<T = string> {
  children: React.ReactNode;
  value: string;
  setValue: Dispatch<SetStateAction<string>>;
  error: T;
  setError: Dispatch<SetStateAction<T>>;
  isAnimationActive: boolean;
  setIsAnimationActive: Dispatch<SetStateAction<boolean>>;
}

const InputBox = ({
  children,
  value,
  setValue,
  error,
  setError,
  isAnimationActive,
  setIsAnimationActive,
}: InputBoxProps) => {
  return (
    <InputContextProvider value={{ value, setValue, error, setError, isAnimationActive, setIsAnimationActive }}>
      {children}
    </InputContextProvider>
  );
};

InputBox.SubmitButton = SubmitButton;
InputBox.Input = Input;
InputBox.ErrorMessage = ErrorMessage;

export default InputBox;
