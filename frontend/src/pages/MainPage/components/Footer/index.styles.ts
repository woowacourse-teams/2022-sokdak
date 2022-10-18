import styled from '@emotion/styled';

export const Container = styled.div`
  width: 80%;
  margin: 60px auto;
  display: flex;

  @media (max-width: 875px) {
    display: none;
  }
`;

export const DividerContainer = styled.div`
  width: 100%;
`;

export const FooterItemContainer = styled.div`
  padding: 1em 0;
  color: ${props => props.theme.colors.gray_300};
`;

export const FooterItem = styled.a`
  color: ${props => props.theme.colors.gray_200};
  background-color: transparent;
  font-size: 13px;
  cursor: pointer;
  margin: 0 10px;
`;
