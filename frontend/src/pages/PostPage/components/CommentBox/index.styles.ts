import { keyframes } from '@emotion/react';
import styled from '@emotion/styled';

const leaveComment = keyframes`
  from {
    background-color: #fafafa;
  }
  to {
    background-color: white;
  }
`;

export const Container = styled.div`
  width: calc(100%-1em);
  padding: 1em 0.5em;
  border-top: 0.5px solid ${props => props.theme.colors.gray_150};
  border-bottom: 0.5px solid ${props => props.theme.colors.gray_150};
  margin-bottom: -0.5px;
  background-color: white;
  animation: ${leaveComment} 0.7s;
`;

export const EmptyComment = styled.div`
  width: 100%;
  border-top: 0.5px solid ${props => props.theme.colors.gray_150};
  border-bottom: 0.5px solid ${props => props.theme.colors.gray_150};
  margin-bottom: -0.5px;
  cursor: default;
  padding: 3.5em 0.5em;
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

export const ButtonContainer = styled.div``;

export const ReplyButton = styled.button`
  background-color: transparent;
  color: ${props => props.theme.colors.gray_200};
  font-size: 0.6rem;
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
  display: flex;
`;

export const PostWriter = styled.span`
  color: ${props => props.theme.colors.sub};
  font-weight: normal;
  font-size: 10px;
  align-self: center;
  margin-left: 5px;
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

export const Footer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

export const LikeContainer = styled.button<{ isLiked: boolean }>`
  display: flex;
  color: ${props => props.theme.colors.pink_300};
  font-size: 0.7rem;
  justify-content: flex-end;
  align-items: center;
  gap: 0.5em;
  background-color: transparent;
  color: ${props => (props.isLiked ? props.theme.colors.pink_300 : props.theme.colors.gray_300)};
`;
