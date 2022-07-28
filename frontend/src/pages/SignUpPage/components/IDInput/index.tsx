import React, { Dispatch, SetStateAction, useLayoutEffect } from 'react';

import InputBox from '@/components/@shared/InputBox';
import { useInput } from '@/components/@shared/InputBox/useInput';

import useIdCheck from '@/hooks/queries/member/useIDCheck';
import useSnackbar from '@/hooks/useSnackbar';

import { SIGN_UP_ERROR } from '@/constants/signUp';
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
  const { showSnackbar } = useSnackbar();
  const { refetch } = useIdCheck({
    storeCode: [value],
    options: {
      onSuccess: data => {
        if (data) {
          showSnackbar(SNACKBAR_MESSAGE.SUCCESS_ID_CHECK);
          setIsSet(true);
        }
        if (!data) {
          setError(SIGN_UP_ERROR.DUPLICATED_ID);
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
      setError(SIGN_UP_ERROR.INVALID_ID);
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
    <InputBox
      value={value}
      setValue={setValue}
      error={error}
      setError={setError}
      isAnimationActive={isAnimationActive}
      setIsAnimationActive={setIsAnimationActive}
    >
      <Styled.InputForm onSubmit={handleIDCheckForm}>
        <InputBox.Input
          handleInvalid={() => {
            setError(SIGN_UP_ERROR.BLANK_ID);
          }}
          placeholder="아이디"
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
