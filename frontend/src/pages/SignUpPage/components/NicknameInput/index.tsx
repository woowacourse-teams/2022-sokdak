import React, { Dispatch, SetStateAction, useContext, useEffect, useLayoutEffect } from 'react';

import InputBox from '@/components/@shared/InputBox';
import { useInput } from '@/components/@shared/InputBox/useInput';

import SnackbarContext from '@/context/Snackbar';

import useNicknameCheck from '@/hooks/queries/member/useNicknameCheck';

import SNACKBAR_MESSAGE from '@/constants/snackbar';
import { isValidNickname } from '@/utils/regExp';

import * as Styled from '../../index.styles';

interface NicknameInputProps extends ReturnType<typeof useInput> {
  isAnimationActive: boolean;
  setIsAnimationActive: Dispatch<SetStateAction<boolean>>;
  isSet: boolean;
  setIsSet: Dispatch<SetStateAction<boolean>>;
}

const NicknameInput = ({
  value,
  setValue,
  error,
  setError,
  isAnimationActive,
  setIsAnimationActive,
  isSet,
  setIsSet,
}: NicknameInputProps) => {
  const { showSnackbar } = useContext(SnackbarContext);
  const { refetch } = useNicknameCheck({
    storeCode: [value],
    options: {
      onSuccess: data => {
        if (data) {
          showSnackbar(SNACKBAR_MESSAGE.SUCCESS_NICKNAME_CHECK);
          setIsSet(true);
        }
        if (!data) {
          setError('중복된 닉네임입니다.');
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
    if (!isValidNickname(value)) {
      setError('닉네임는 4자에서 16자 사이입니다.');
    }
    if (isValidNickname(value)) {
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
            setError('닉네임를 입력해주세요');
          }}
          placeholder="닉네임"
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

export default NicknameInput;
