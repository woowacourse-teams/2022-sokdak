import { Link } from 'react-router-dom';

import Comment from '@/assets/images/comment.svg';
import Heart from '@/assets/images/heart.svg';

import styled from '@emotion/styled';

export const Title = styled.p`
  font-family: 'BMHANNAPro', 'Noto Sans KR';
  font-size: 1.5rem;
  margin-bottom: 0.5em;
`;

export const ItemTitle = styled.p`
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
  line-height: initial;
`;

export const Item = styled(Link)`
  width: calc(100%-2em);
  display: flex;
  justify-content: space-between;
  font-family: 'BMHANNAPro', 'Noto Sans KR';
  padding: 1em 0;
  align-items: center;
`;

export const ItemContainer = styled.section`
  display: flex;
  width: 100%;
  flex-direction: column;
  ${Item} {
    border-bottom: 1.5px solid ${props => props.theme.colors.gray_50};
  }
  ${Item}:last-child {
    border: none;
  }
`;

export const PostInfoContainer = styled.div`
  display: flex;
  gap: 10px;
  font-family: 'NotoSansKR';
`;

export const IconContainer = styled.div`
  min-width: 2.5rem;
`;

export const LikeIcon = styled(Heart)`
  fill: ${props => props.theme.colors.pink_300};
  stroke: ${props => props.theme.colors.pink_300};
  width: 16px;
  height: 14px;
  margin: 0 4px 0 0;
`;

export const LikeCount = styled.span`
  color: ${props => props.theme.colors.pink_300};
`;

export const CommentIcon = styled(Comment)`
  width: 16px;
  height: 15px;
  margin: 0 4px 0 0;
`;

export const CommentCount = styled.span`
  color: ${props => props.theme.colors.gray_200};
`;

export const LoadMoreButton = styled(Link)`
  display: block;
  width: 100%;
  padding: 1em 0em;
  text-align: center;
  color: ${props => props.theme.colors.gray_300};
  border: 1px solid ${props => props.theme.colors.gray_50};
`;
