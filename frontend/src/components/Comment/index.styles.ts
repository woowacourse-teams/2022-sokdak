import styled from '@emotion/styled';

export const Container = styled.div`
  width: 100%;
  border-top: 0.5px solid ${props => props.theme.colors.gray_150};
  padding: 1em;
`;

export const Nickname = styled.p`
  font-weight: bold;
  margin-bottom: 1em;
`;

export const Content = styled.p`
  font-size: 12px;
`;

export const Date = styled.p`
  color: ${props => props.theme.colors.gray_200};
  margin: 10px 0;
  font-size: 10px;
`;
