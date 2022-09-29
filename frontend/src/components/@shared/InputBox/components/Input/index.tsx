import { InputHTMLAttributes } from 'react';

import * as Styled from './index.styles';

import { useInputContext } from '../../useInputContext';

interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
  hasError?: boolean;
  handleInvalid: () => void;
}

const Input = ({ placeholder, type, handleInvalid, required, onChange, onKeyDown, disabled, onBlur }: InputProps) => {
  const { value, setValue, error, setError, isAnimationActive, setIsAnimationActive } = useInputContext();
  return (
    <Styled.Input
      value={value}
      onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
        setValue(e.currentTarget.value);
        if (onChange) onChange(e);
      }}
      hasError={error !== ''}
      placeholder={placeholder}
      type={type}
      onInvalid={e => {
        e.preventDefault();
        if (setIsAnimationActive) setIsAnimationActive(true);
        handleInvalid();
      }}
      onFocus={() => {
        if (error) {
          setValue('');
        }
      }}
      isAnimationActive={isAnimationActive}
      onKeyDown={(e: React.KeyboardEvent<HTMLInputElement>) => {
        if (onKeyDown) onKeyDown(e);
        if (!onKeyDown) setError('');
      }}
      required={required}
      onAnimationEnd={() => {
        if (setIsAnimationActive) setIsAnimationActive(false);
      }}
      disabled={disabled}
      onBlur={e => {
        if (onBlur) onBlur(e);
      }}
    />
  );
};

export default Input;
