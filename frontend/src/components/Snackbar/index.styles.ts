import styled from '@emotion/styled';

export const Container = styled.div`
  width: 300px;
  height: 50px;
  background-color: ${props => props.theme.colors.gray_300};
  border-radius: 7px;
  color: white;
  font-size: 11px;
  display: flex;
  align-items: center;
  justify-content: center;
`;
