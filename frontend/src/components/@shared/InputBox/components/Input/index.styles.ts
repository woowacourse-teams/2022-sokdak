import { keyframes } from '@emotion/react';
import styled from '@emotion/styled';

export const Input = styled.input<{ hasError: boolean; isAnimationActive?: boolean }>`
  border-bottom: 1px solid ${props => (props.hasError ? props.theme.colors.red_100 : props.theme.colors.gray_50)};
  width: 100%;
  display: inline-block;
  padding: 8px 0px 10px 8px;
  animation: ${props => (props.isAnimationActive ? huduldul : null)} 0.3s;
`;

// animation 재활용 필요
export const huduldul = keyframes`
  0%{
    transform:translateX(-5px)
  }20%{
    transform:translateX(5px)
  }40%{
    transform:translateX(-5px)
  }60%{
    transform:translateX(5px)
  }100%{
    transform:translateX(0px)
  }
`;
