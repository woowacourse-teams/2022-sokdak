import CheckBoxComponent from '@/components/CheckBox';

import Camera from '@/assets/images/camera.svg';

import { invalidInputAnimation } from '@/style/GlobalStyle';
import { keyframes } from '@emotion/react';
import styled from '@emotion/styled';

export const Container = styled.form`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  padding: 0 13px;
  box-sizing: border-box;
`;

export const Heading = styled.h1`
  font-family: 'BMHANNAPro', 'Noto Sans KR';
  font-size: 27px;
`;

export const Board = styled.p`
  font-family: 'BMHANNAAir', 'Noto Sans KR';
  margin: 15px 0 30px 0;
  color: ${props => props.theme.colors.gray_200};
`;

interface InputProps {
  isValid: boolean;
  isAnimationActive: boolean;
}

export const TitleInput = styled.input<InputProps>`
  font-family: 'BMHANNAAir', 'Noto Sans KR';
  border-bottom: 1px solid ${props => (props.isValid ? props.theme.colors.sub : props.theme.colors.red_100)};

  width: 100%;
  padding: 10px;
  font-size: 20px;
  ${invalidInputAnimation}

  :valid {
    border-bottom: 1px solid ${props => props.theme.colors.sub};
  }
`;

export const ContentInput = styled.textarea<InputProps>`
  width: 100%;
  height: 290px;
  padding: 10px;
  font-size: 14px;
  margin: 20px 0;
  ${invalidInputAnimation}

  ::placeholder {
    color: ${props => (props.isValid ? 'gray' : props.theme.colors.red_100)};
  }

  :valid {
    ::placeholder {
      color: grey;
    }
  }
`;

export const SubmitButton = styled.button`
  font-family: 'BMHANNAAir', 'Noto Sans KR';
  background-color: ${props => props.theme.colors.sub};
  color: white;
  border-radius: 15px;
  font-size: 17px;
  width: 100%;
  height: 55px;
  cursor: pointer;
`;

export const CheckBox = styled(CheckBoxComponent)``;

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

export const rotate = keyframes`
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

export const PostController = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
`;

export const CameraIcon = styled(Camera)``;

export const ImageInput = styled.input`
  display: none;
`;

export const ImageUploadButton = styled.label`
  cursor: pointer;
  padding-left: 10px;
  padding-right: 10px;
  margin-right: -10px;
`;
