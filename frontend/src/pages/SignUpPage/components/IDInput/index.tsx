import React, { Dispatch, SetStateAction, useContext, useLayoutEffect } from 'react';

import InputBox from '@/components/@shared/InputBox';
import { useInput } from '@/components/@shared/InputBox/useInput';

import SnackbarContext from '@/context/Snackbar';

import useIdCheck from '@/hooks/queries/member/useIDCheck';

import SNACKBAR_MESSAGE from '@/constants/snackbar';
import { isValidID } from '@/utils/regExp';

import * as Styled from '../../index.styles';

interface IDInputProps extends ReturnType<typeof useInput> {
  isAnimationActive: boolean;
  setIsAnimationActive: Dispatch<SetStateAction<boolean>>;
  isSet: boolean;
  setIsSet: Dispatch<SetStateAction<boolean>>;
}

const IDInput = ({
  value,
  setValue,
  error,
  setError,
  isAnimationActive,
  setIsAnimationActive,
  isSet,
  setIsSet,
}: IDInputProps) => {
  const { showSnackbar } = useContext(SnackbarContext);
  const { refetch } = useIdCheck({
    storeCode: [value],
    options: {
      onSuccess: data => {
        if (data) {
          showSnackbar(SNACKBAR_MESSAGE.SUCCESS_ID_CHECK);
          setIsSet(true);
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

  useLayoutEffect(() => {
    if (!value) {
      return;
    }
    setIsSet(false);
    if (!isValidID(value)) {
      setError('아이디는 4자에서 16자 사이입니다.');
    }
    if (isValidID(value)) {
      setError('');
    }
  }, [value]);

  const handleIDCheckForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (isSet) return;
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
          isAnimationActive={isAnimationActive}
          setIsAnimationActive={setIsAnimationActive}
          required
        />
        <InputBox.SubmitButton disabled={error !== '' || value === ''}>
          {isSet ? '중복 확인 완료' : '중복 확인'}
        </InputBox.SubmitButton>
      </Styled.InputForm>
      <InputBox.ErrorMessage />
    </InputBox>
  );
};

export default IDInput;
