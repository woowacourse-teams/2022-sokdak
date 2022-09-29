import { css } from '@emotion/react';
import styled from '@emotion/styled';

const DividerStyle = css`
  width: 1.5px;
  height: 1.5px;
`;

const Divider = styled.div<{ width?: number; height?: number; horizontal: boolean }>`
  ${DividerStyle}
  width: ${props => (props.horizontal ? '100%' : props.width)};
  height: ${props => (props.horizontal ? props.height : props.height)};
  background-color: ${props => props.theme.colors.gray_50};
  box-sizing: border-box;
`;

export default Divider;
