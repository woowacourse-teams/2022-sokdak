import { invalidInputAnimation } from '@/style/GlobalStyle';
import styled from '@emotion/styled';

export const Input = styled.input<{ hasError: boolean; isAnimationActive?: boolean }>`
  border-bottom: 1px solid ${props => (props.hasError ? props.theme.colors.red_100 : props.theme.colors.gray_50)};
  width: 100%;
  display: inline-block;
  padding: 8px 0px 10px 8px;
  ${invalidInputAnimation}

  ::placeholder {
    color: ${props => props.theme.colors.gray_150};
  }
`;
