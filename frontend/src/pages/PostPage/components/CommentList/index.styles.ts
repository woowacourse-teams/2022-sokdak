import CommentBox from '../CommentBox';
import styled from '@emotion/styled';

export const Container = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  padding: 30px 0;
`;

export const CommentsContainer = styled.div`
  width: 100%;
  margin-top: 50px;
  display: flex;
  flex-direction: column;

  :last-child {
    border-bottom: 0.5px solid ${props => props.theme.colors.gray_150};
  }
`;

export const ReplyBox = styled(CommentBox)`
  width: 85%;
  align-self: flex-end;
`;
