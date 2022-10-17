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
  padding: 1em;
  color: ${props => props.theme.colors.gray_300};
`;

export const FooterItem = styled.button`
  color: ${props => props.theme.colors.gray_300};
  background-color: transparent;
`;
