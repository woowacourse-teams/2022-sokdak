import { Link } from 'react-router-dom';

import { keyframes } from '@emotion/react';
import styled from '@emotion/styled';

export const InputForm = styled.form`
  width: 100%;
  display: grid;
  grid-template-columns: 4fr 1fr;
  align-items: center;
`;

export const SignUpForm = styled.div`
  display: flex;
  width: 100%;
  flex-direction: column;
  align-items: center;
`;

export const Heading = styled.h1`
  font-family: 'BMHANNAPro';
  font-size: 27px;
  margin: 40px 0 70px;
`;

export const SubmitButton = styled.button`
  width: 100%;
  font-family: 'BMHANNAAir';
  background-color: ${props => props.theme.colors.sub};
  color: white;
  border-radius: 15px;
  font-size: 17px;
  width: 100%;
  height: 55px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 30px;
  :disabled {
    background-color: ${props => props.theme.colors.gray_300};
    cursor: not-allowed;
  }
`;

export const LoginLink = styled(Link)`
  display: inline;
  color: ${props => props.theme.colors.sub};
  cursor: pointer;
`;

export const SignUpText = styled.p`
  width: 100%;
  padding: 40px 0px 30px;
  text-align: center;
  cursor: default;
  border-bottom: 1px solid ${props => props.theme.colors.gray_50};
`;

const sizeUp = keyframes`
  from {
    display: none;
    height: 0;
    opacity: 0;
  }
  to {
    display: block;
    height: 65px;
    opacity: 1;
  }
`;

const sizeDown = keyframes`
  from {
    display: block;
    height: 65px;
    opacity: 1;
  }
  to {
    display: none;
    height: 0;
    opacity: 0;
  }
`;

export const VerificationCodeContainer = styled.div<{ isEmailSet: boolean; isVerified: boolean }>`
  width: 100%;

  animation: ${props => (props.isEmailSet ? (props.isVerified ? sizeDown : sizeUp) : null)} 0.5s;
  animation-fill-mode: forwards;
`;

export const PasswordInputContainer = styled.div`
  width: 100%;
  display: grid;
  grid-template-columns: 7fr 1fr;
  align-items: center;
`;

export const MessageContainer = styled.div`
  width: 100%;
  padding: 10px;
  height: 10px;
`;

export const Message = styled.p`
  font-size: 12px;
  color: ${props => props.theme.colors.gray_200};
`;
