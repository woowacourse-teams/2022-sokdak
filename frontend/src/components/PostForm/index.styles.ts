import CheckBoxComponent from '@/components/CheckBox';

import { invalidInputAnimation } from '@/style/GlobalStyle';
import styled from '@emotion/styled';

export const Container = styled.form`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: calc(100% - 2em);
  padding: 1em;
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
  height: 50vh;
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

export const PostController = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
`;
