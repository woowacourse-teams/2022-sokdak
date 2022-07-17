import { css } from '@emotion/react';
import styled from '@emotion/styled';

export const Modal = styled.div`
  width: 260px;
  height: 140px;
  background: #ffffff;
  box-shadow: 0px 1px 7px rgba(0, 0, 0, 0.25);
  border-radius: 4px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  padding: 20px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  z-index: 20;
`;

export const Dimmer = styled.div`
  width: 100%;
  height: 100%;
  background-color: black;
  opacity: 0.2;
  position: absolute;
  top: 0px;
  left: 0px;
  z-index: 10;
`;

export const Title = styled.p`
  font-weight: 700;
  font-size: 18px;
`;

export const Notice = styled.p`
  font-weight: 400;
  font-size: 13px;
  color: ${props => props.theme.colors.gray_200};
`;

export const ButtonContainer = styled.div`
  display: flex;
  justify-content: flex-end;
`;

const button = () => css`
  width: 58px;
  height: 28px;
  border-radius: 4px;
  font-size: 10px;
  margin-left: 10px;
`;

export const ConfirmButton = styled.button`
  ${button}

  background: ${props => props.theme.colors.sub};
  color: white;
`;

export const CancelButton = styled.button`
  ${button}

  background: white;
  color: ${props => props.theme.colors.sub};
`;
