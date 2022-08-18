import PandaIcon from '@/assets/images/panda_logo.svg';

import { keyframes, css } from '@emotion/react';
import styled from '@emotion/styled';

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 500px;
  gap: 2em;
`;

export const ErrorCode = styled.p`
  font-family: 'BMHANNAPro', 'Noto Sans KR';
  font-size: 80px;
`;

export const ErrorMessage = styled.p`
  font-family: 'BMHANNAPro', 'Noto Sans KR';
  font-size: 20px;
  line-height: 25px;
`;

const upDown = keyframes`
  0% {
    transform: translateY(-5px);
  }
  25% {
    transform: translateY(5px);
  }
  50% {
    transform: translateY(-5px);
  }
  75% {
    transform: translateY(5px);
  }
  100% {
    transform: translateY(-5px);
  }
`;

const rotate = keyframes`
  0% {
    transform: rotate(0deg);
  }
  25% {
    transform: rotate(90deg);
  }
  50% {
    transform: rotate(180deg);
  }
  75% {
    transform: rotate(270deg);
  }
  100% {
    transform: rotate(360deg);
  }
`;

export const fixed = css`
  position: fixed;
  top: 500px;
`;

export const Panda = styled(PandaIcon)`
  width: 80px;
  height: 80px;

  animation-duration: 1s;
  animation-name: ${upDown};
  animation-iteration-count: infinite;
  cursor: pointer;

  :hover {
    animation-name: ${rotate};
    animation-timing-function: linear;
  }
`;
