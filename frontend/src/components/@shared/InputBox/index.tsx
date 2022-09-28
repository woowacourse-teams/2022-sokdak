import { PropsWithChildrenC, StateAndAction } from 'sokdak-util-types';

import ErrorMessage from './components/ErrorMessage';
import Input from './components/Input';
import SubmitButton from './components/SubmitButton';

import { InputContextProvider } from './useInputContext';

interface InputBoxProps<T = string>
  extends StateAndAction<string, 'value'>,
    StateAndAction<boolean, 'isAnimationActive'>,
    StateAndAction<T, 'error'> {}

const InputBox = ({
  children,
  value,
  setValue,
  error,
  setError,
  isAnimationActive,
  setIsAnimationActive,
}: PropsWithChildrenC<InputBoxProps>) => {
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
