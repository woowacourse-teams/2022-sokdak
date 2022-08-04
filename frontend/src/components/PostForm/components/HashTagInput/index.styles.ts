import { keyframes } from '@emotion/react';
import styled from '@emotion/styled';

export const Container = styled.div`
  width: 100%;
  background-color: ${props => props.theme.colors.gray_10};
  border-radius: 15px;
  min-height: 55px;
  margin: 20px 0;
  float: left;
  display: flex;
  padding: 15px;
  box-sizing: border-box;
  flex-wrap: wrap;
  gap: 5px;
  row-gap: 10px;
  position: relative;
`;

export const appear = keyframes`
  0% {
    opacity: 0;
    top: -45px;
  } 
  100% {
    opacity: 0.6;
    top: -65px;
  }
`;

export const Tooltip = styled.div`
  position: absolute;
  background-color: black;
  opacity: 0.75;
  width: 115px;
  height: 60px;
  top: -67px;
  left: 0px;
  color: white;
  font-size: 10px;
  line-height: 18px;
  display: flex;
  justify-content: center;
  align-items: center;
  animation: ${appear} 0.3s;
`;

export const Input = styled.input`
  background-color: transparent;
  width: 130px;

  ::placeholder {
    font-size: 14px;
    color: ${props => props.theme.colors.gray_200};
  }
`;
