import styled from '@emotion/styled';

export const Button = styled.button`
  background-color: inherit;
  font-family: 'BMHANNAAir';
  color: ${props => props.theme.colors.sub};
  border: none;
  cursor: pointer;
  min-width: 100px;
  height: 30px;
  :disabled {
    color: ${props => props.theme.colors.gray_300};
    cursor: not-allowed;
  }
`;
