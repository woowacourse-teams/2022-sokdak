import { keyframes } from '@emotion/react';
import styled from '@emotion/styled';

export const ImagePreview = styled.div`
  width: 100%;
  height: 100px;
  display: flex;
`;

export const Image = styled.img`
  width: 100px;
  height: 100px;
  border-radius: 5px;
`;

export const ImageName = styled.p`
  font-size: 14px;
  color: ${props => props.theme.colors.gray_200};
  padding-left: 20px;
  line-height: 20px;
  user-select: none;
`;

export const ImageUploadLoading = styled.div`
  width: 100%;
  height: 100px;
  color: ${props => props.theme.colors.gray_200};
  display: flex;
  justify-content: center;
  align-items: center;
  font-family: 'BMHANNAAir', 'Noto Sans KR';
  font-size: 18px;
`;

const rotate = keyframes`
  0% {
    transform: rotate(0deg);
  } 
  100% {
    transform: rotate(360deg);
  }
`;

export const LoadingIcon = styled.span`
  animation: ${rotate} 1.5s linear 0s infinite;
  margin-left: 10px;
`;
