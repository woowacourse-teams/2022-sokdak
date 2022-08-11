import styled from '@emotion/styled';

export const Container = styled.div`
  display: flex;
`;

export const Page = styled.button<{ isCurrentPage: boolean }>`
  background-color: transparent;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;

  font-weight: 800;
  font-size: 14px;
  color: ${props => (props.isCurrentPage ? props.theme.colors.sub : props.theme.colors.gray_200)};
`;
