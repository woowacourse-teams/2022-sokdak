import { invalidInputAnimation } from '@/style/GlobalStyle';
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
  font-family: 'BMHANNAPro';
  font-size: 27px;
  margin: 40px 0;
`;

interface InputProps {
  isValid: boolean;
  isAnimationActive: boolean;
}

export const TitleInput = styled.input<InputProps>`
  font-family: 'BMHANNAAir';
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
  font-family: 'BMHANNAAir';
  background-color: ${props => props.theme.colors.sub};
  color: white;
  border-radius: 15px;
  font-size: 17px;
  width: 100%;
  height: 55px;
  cursor: pointer;
`;
