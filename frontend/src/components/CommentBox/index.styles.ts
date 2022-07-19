import styled from '@emotion/styled';

export const Container = styled.div`
  width: calc(100%-1em);
  border-top: 0.5px solid ${props => props.theme.colors.gray_150};
  padding: 1em 0.5em;
`;

export const Nickname = styled.p`
  font-weight: bold;
  margin-bottom: 1em;
`;

export const Content = styled.p`
  font-size: 12px;
  line-height: 1.3;
  white-space: pre-wrap;
`;

export const Date = styled.p`
  color: ${props => props.theme.colors.gray_200};
  margin: 10px 0;
  font-size: 10px;
`;
