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
}

const InputBox = ({ children, value, setValue, error, setError }: InputBoxProps) => {
  return <InputContextProvider value={{ value, setValue, error, setError }}>{children}</InputContextProvider>;
};

InputBox.SubmitButton = SubmitButton;
InputBox.Input = Input;
InputBox.ErrorMessage = ErrorMessage;

export default InputBox;
