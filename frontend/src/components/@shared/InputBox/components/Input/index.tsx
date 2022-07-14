import React from 'react';

import * as Styled from './index.styles';

import { useInputContext } from '../../useInputContext';

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  hasError?: boolean;
}

const Input = ({ placeholder, type, onInvalid }: InputProps) => {
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
      onInvalid={onInvalid}
      onFocus={() => {
        setValue('');
      }}
      onKeyDown={() => {
        setError('');
      }}
    />
  );
};

export default Input;
