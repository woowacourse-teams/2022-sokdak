import styled from '@emotion/styled';

export const Container = styled.div`
  width: calc(100%-1em);
  padding: 1em 0.5em;
  border-top: 0.5px solid ${props => props.theme.colors.gray_150};
`;

export const BlockContainer = styled.div`
  width: 100%;
  border-top: 0.5px solid ${props => props.theme.colors.gray_150};
  cursor: default;
  padding: 2em 0;
`;

export const BlockedContent = styled.p`
  color: ${props => props.theme.colors.gray_200};
  font-size: 12px;
`;

export const CommentHeader = styled.div`
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1em;
`;

export const DeleteButton = styled.button`
  background-color: transparent;
  color: ${props => props.theme.colors.red_200};
  font-size: 0.6rem;
`;
export const ReportButton = styled.button`
  background-color: transparent;
`;

export const Nickname = styled.p`
  font-weight: bold;
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
