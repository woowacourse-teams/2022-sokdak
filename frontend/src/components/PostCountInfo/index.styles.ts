import Comment from '@/assets/images/comment.svg';
import Heart from '@/assets/images/heart.svg';

import styled from '@emotion/styled';

export const Container = styled.div`
  max-width: fit-content;
  align-self: flex-end;
  display: flex;
  justify-content: space-between;
  font-family: 'Noto Sans KR', sans-serif;
`;

export const LikeIcon = styled(Heart)`
  fill: ${props => props.theme.colors.pink_300};
  stroke: ${props => props.theme.colors.pink_300};
  width: 16px;
  height: 14px;
  margin: 0 4px 0 10px;
`;

export const LikeCount = styled.span`
  color: ${props => props.theme.colors.pink_300};
`;

export const CommentIcon = styled(Comment)`
  width: 16px;
  height: 15px;
  margin: 0 4px 0 10px;
`;

export const CommentCount = styled.span`
  color: ${props => props.theme.colors.gray_200};
`;
