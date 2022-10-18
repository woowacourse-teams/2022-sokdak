import { css, keyframes } from '@emotion/react';
import styled from '@emotion/styled';

const up = keyframes`
  0% {
    opacity: 0;
    transform: translateY(200px);
    animation-timing-function: cubic-bezier(1, 0, 0.8, 1)
  } 
  100% {
    opacity: 1;
    transform: translateY(0);
  }
`;

export const Container = styled.div`
  display: flex;
  position: fixed;
  bottom: 0px;
  left: calc(50% - 175px);
  z-index: 10;
  width: 350px;
  height: 200px;
  background-color: white;
  padding: 30px;
  border-radius: 10px 10px 0 0;
  flex-direction: column;
  justify-content: space-between;
  box-sizing: border-box;
  box-shadow: 0px -7px 13px -6px rgb(0 0 0 / 20%);
  animation: ${up} 0.8s;
  user-select: none;
`;

export const Message = styled.div`
  font-weight: bold;
  font-size: 17px;
`;

export const ButtonContainer = styled.div``;

export const Controller = styled.div`
  display: flex;
  justify-content: space-between;
`;

const button = () => css`
  width: 130px;
  height: 40px;
  border-radius: 4px;
  font-size: 14px;
  background-color: transparent;
`;

export const Confirm = styled.button`
  ${button}
  background-color: ${props => props.theme.colors.sub};
  color: white;
`;

export const Cancel = styled.button`
  ${button}
  color: ${props => props.theme.colors.sub};
`;

export const Hide = styled.button`
  background-color: transparent;
  color: ${props => props.theme.colors.gray_200};
  width: 100%;
  text-align: left;
  margin-top: 2px;
  font-size: 11px;
`;
