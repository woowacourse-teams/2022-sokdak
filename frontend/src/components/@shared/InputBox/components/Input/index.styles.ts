import styled from '@emotion/styled';

export const Input = styled.input<{ hasError: boolean }>`
  border-bottom: 1px solid ${props => (props.hasError ? props.theme.colors.red_100 : props.theme.colors.gray_50)};
  width: 100%;
  display: inline-block;
  padding: 8px 0px 10px 8px;
`;
