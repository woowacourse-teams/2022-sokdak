import React, { Dispatch, SetStateAction, useLayoutEffect } from 'react';

import InputBox from '@/components/@shared/InputBox';
import { useInput } from '@/components/@shared/InputBox/useInput';

import useNicknameCheck from '@/hooks/queries/member/useNicknameCheck';
import useSnackbar from '@/hooks/useSnackbar';

import { SIGN_UP_ERROR } from '@/constants/signUp';
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
  const { showSnackbar } = useSnackbar();
  const { refetch } = useNicknameCheck({
    storeCode: [value],
    options: {
      onSuccess: data => {
        if (data) {
          showSnackbar(SNACKBAR_MESSAGE.SUCCESS_NICKNAME_CHECK);
          setIsSet(true);
        }
        if (!data) {
          setError(SIGN_UP_ERROR.DUPLICATED_NICKNAME);
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
      setError(SIGN_UP_ERROR.INVALID_NICKNAME);
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
            setError(SIGN_UP_ERROR.BLANK_NICKNAME);
          }}
          placeholder="닉네임"
          required
        />
        <InputBox.SubmitButton disabled={error !== '' || value === ''}>
          {isSet ? '중복 확인 완료' : '중복 확인'}
        </InputBox.SubmitButton>
        {error ? (
          <InputBox.ErrorMessage />
        ) : (
          <Styled.MessageContainer>
            <Styled.Message>테크코스 닉네임은 익명성을 헤칠수 있습니다.</Styled.Message>
          </Styled.MessageContainer>
        )}
      </Styled.InputForm>
    </InputBox>
  );
};

export default NicknameInput;
