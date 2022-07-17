import React, { Dispatch, SetStateAction, useContext } from 'react';

import InputBox from '@/components/@shared/InputBox';
import { useInput } from '@/components/@shared/InputBox/useInput';

import SnackbarContext from '@/context/Snackbar';

import useIdCheck from '@/hooks/queries/member/useIDCheck';

import { isValidID } from '@/utils/regExp';

import * as Styled from '../index.styles';

interface IDInputProps extends ReturnType<typeof useInput> {
  isAnimationActive: boolean;
  setIsAnimationActive: Dispatch<SetStateAction<boolean>>;
}

const IDInput = ({ value, setValue, error, setError, isAnimationActive, setIsAnimationActive }: IDInputProps) => {
  const { showSnackbar } = useContext(SnackbarContext);
  const { refetch } = useIdCheck({
    storeCode: [value],
    options: {
      onSuccess: data => {
        if (data) {
          showSnackbar('사용할 수 있는 아이디입니다.');
        }
        if (!data) {
          setError('중복된 아이디입니다.');
          setIsAnimationActive(true);
        }
      },
      onError(error) {
        setError(error.response?.data.message!);
        setIsAnimationActive(true);
      },
      enabled: false,
    },
  });

  const handleChangeIDInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (!isValidID(value)) {
      setError('아이디는 4자에서 16자 사이입니다.');
    }
    if (isValidID(value)) {
      setError('');
    }
  };

  const handleIDCheckForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    refetch();
  };

  return (
    <InputBox value={value} setValue={setValue} error={error} setError={setError}>
      <Styled.InputForm onSubmit={handleIDCheckForm}>
        <InputBox.Input
          handleInvalid={() => {
            setError('아이디를 입력해주세요');
          }}
          placeholder="아이디"
          onChange={handleChangeIDInput}
          isAnimationActive={isAnimationActive}
          setIsAnimationActive={setIsAnimationActive}
          required
        />
        <InputBox.SubmitButton disabled={error !== '' || value === ''}>중복 확인</InputBox.SubmitButton>
      </Styled.InputForm>
      <InputBox.ErrorMessage />
    </InputBox>
  );
};

export default IDInput;
