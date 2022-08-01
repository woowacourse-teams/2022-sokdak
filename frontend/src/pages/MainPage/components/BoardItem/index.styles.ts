import { Link } from 'react-router-dom';

import Comment from '@/assets/images/comment.svg';
import Heart from '@/assets/images/heart.svg';

import styled from '@emotion/styled';

export const Title = styled.p`
  font-family: 'BMHANNAPro';
  font-size: 1.5rem;
  margin-bottom: 0.5em;
`;

export const Item = styled(Link)`
  width: calc(100%-2em);
  display: flex;
  justify-content: space-between;
  font-family: 'BMHANNAPro';
  padding: 1em;
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
  align-self: flex-end;
  display: flex;
  justify-content: space-between;
  gap: 1px;
  font-family: 'NotoSansKR';
  opacity: 0.7;
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

export const LoadMoreButton = styled(Link)`
  display: block;
  width: 100%;
  padding: 1em 0em;
  text-align: center;
  color: ${props => props.theme.colors.gray_300};
  border: 1px solid ${props => props.theme.colors.gray_50};
`;
