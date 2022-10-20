import styled from '@emotion/styled';

export const Container = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
`;

export const Title = styled.h1`
  margin: 2.5rem 0;
  font-size: 14px;
  color: ${props => props.theme.colors.gray_200};
`;

export const Highlight = styled.span`
  color: ${props => props.theme.colors.sub};
  font-weight: 700;
`;
