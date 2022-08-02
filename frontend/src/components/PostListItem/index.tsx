import { forwardRef } from 'react';

import * as Styled from './index.styles';

import countFormatter from '@/utils/countFormatter';
import timeConverter from '@/utils/timeConverter';

interface PostListItemProps extends Omit<Post, 'id' | 'like' | 'hashtags' | 'authorized' | 'boardId'> {
  handleClick: (e: React.MouseEvent<HTMLDivElement, MouseEvent>) => void;
  testid: number;
}

const PostListItem = forwardRef<HTMLDivElement, PostListItemProps>(
  (
    { title, content, createdAt, likeCount, commentCount, modified, handleClick, testid, blocked }: PostListItemProps,
    ref,
  ) => {
    if (blocked) {
      return (
        <Styled.BlockedContainer ref={ref} data-testid={testid}>
          <Styled.HeadContainer>
            <Styled.TitleContainer>
              <Styled.Title isModified={false}>블라인드 처리된 글입니다.</Styled.Title>
            </Styled.TitleContainer>
          </Styled.HeadContainer>
          <Styled.ContentContainer>
            <Styled.Content>블라인드 처리된 글입니다.</Styled.Content>
          </Styled.ContentContainer>
          <Styled.PostInfoContainer>
            <Styled.LikeIcon />
            <Styled.LikeCount>{countFormatter(likeCount)}</Styled.LikeCount>
            <Styled.CommentIcon />
            <Styled.CommentCount>{countFormatter(commentCount)}</Styled.CommentCount>
          </Styled.PostInfoContainer>
        </Styled.BlockedContainer>
      );
    }
    return (
      <Styled.Container onClick={handleClick} ref={ref} data-testid={testid}>
        <Styled.HeadContainer>
          <Styled.TitleContainer>
            <Styled.Title isModified={modified}>{title}</Styled.Title>
            {modified && <Styled.Modified>(편집됨)</Styled.Modified>}
          </Styled.TitleContainer>
          <Styled.Date>{timeConverter(createdAt)}</Styled.Date>
        </Styled.HeadContainer>
        <Styled.ContentContainer>
          <Styled.Content>{content}</Styled.Content>
        </Styled.ContentContainer>
        <Styled.PostInfoContainer>
          <Styled.LikeIcon />
          <Styled.LikeCount>{countFormatter(likeCount)}</Styled.LikeCount>
          <Styled.CommentIcon />
          <Styled.CommentCount>{countFormatter(commentCount)}</Styled.CommentCount>
        </Styled.PostInfoContainer>
      </Styled.Container>
    );
  },
);

export default PostListItem;
