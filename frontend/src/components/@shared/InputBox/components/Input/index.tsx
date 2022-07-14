import React from 'react';

import * as Styled from './index.styles';

import { useInputContext } from '../../useInputContext';

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  hasError?: boolean;
  handleInvalid: () => void;
}

const Input = ({ placeholder, type, handleInvalid, required }: InputProps) => {
  const { value, setValue, error, setError } = useInputContext();
  return (
    <Styled.Input
      value={value}
      onChange={e => {
        setValue(e.currentTarget.value);
      }}
      hasError={error !== ''}
      placeholder={placeholder}
      type={type}
      onInvalid={e => {
        e.preventDefault();
        handleInvalid();
      }}
      onFocus={() => {
        setValue('');
      }}
      onKeyDown={() => {
        setError('');
      }}
      required={required}
    />
  );
};

export default Input;
