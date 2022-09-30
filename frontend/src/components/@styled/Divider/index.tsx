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
  box-shadow: 1px 1px 0.7px 0.3px ${props => props.theme.colors.gray_50};
  box-sizing: border-box;
`;

export default Divider;
