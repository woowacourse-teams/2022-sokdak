import { css, keyframes } from '@emotion/react';
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
  width: 100%;
  padding: 1.8em 15px;
  border-top: 0.5px solid ${props => props.theme.colors.gray_150};
  background-color: white;
  animation: ${leaveComment} 0.7s;
  font-size: 10px;
  box-sizing: border-box;

  @media (min-width: 875px) {
    font-size: 13px;
  }
`;

export const EmptyComment = styled.div`
  width: 100%;
  border-top: 0.5px solid ${props => props.theme.colors.gray_150};
  border-bottom: 0.5px solid ${props => props.theme.colors.gray_150};
  margin-bottom: -0.5px;
  cursor: default;
  padding: 3.5rem 0.5rem;
  color: ${props => props.theme.colors.gray_200};
  font-size: 0.75rem;

  @media (min-width: 875px) {
    font-size: 0.975rem;
  }
`;

export const CommentHeader = styled.div`
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
`;

export const ButtonContainer = styled.div``;

const buttonStyle = css`
  background-color: transparent;
  font-size: 1em;
`;

export const ReplyButton = styled.button`
  ${buttonStyle};
  color: ${props => props.theme.colors.gray_200};
`;

export const DeleteButton = styled.button`
  ${buttonStyle};
  color: ${props => props.theme.colors.red_200};
`;

export const ReportButton = styled.button`
  ${buttonStyle};
`;

export const Nickname = styled.p`
  font-weight: bold;
  display: flex;
  font-size: 1.3em;
`;

export const PostWriter = styled.span`
  color: ${props => props.theme.colors.sub};
  font-weight: normal;
  font-size: 10px;
  align-self: center;
  margin-left: 5px;
`;

export const Content = styled.p`
  font-size: 1.2em;
  line-height: 1.3;
  white-space: pre-wrap;
`;

export const Date = styled.p`
  color: ${props => props.theme.colors.gray_200};
  margin: 10px 0;
  font-size: 1em;
`;

export const Footer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

export const LikeContainer = styled.button<{ isLiked: boolean }>`
  display: flex;
  color: ${props => props.theme.colors.pink_300};
  font-size: 1em;
  justify-content: flex-end;
  align-items: center;
  gap: 0.5rem;
  background-color: transparent;
  color: ${props => (props.isLiked ? props.theme.colors.pink_300 : props.theme.colors.gray_300)};
`;
