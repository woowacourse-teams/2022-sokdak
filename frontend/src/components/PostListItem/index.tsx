import { forwardRef } from 'react';

import PostCountInfo from '@/components/PostCountInfo';

import * as Styled from './index.styles';

import timeConverter from '@/utils/timeConverter';

interface PostListItemProps
  extends Omit<Post, 'id' | 'like' | 'hashtags' | 'authorized' | 'boardId' | 'nickname' | 'imageName'> {
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
            <Styled.TitleContainer isModified={false}>
              <Styled.Title>블라인드 처리된 글입니다.</Styled.Title>
            </Styled.TitleContainer>
          </Styled.HeadContainer>
          <Styled.ContentContainer>
            <Styled.Content>블라인드 처리된 글입니다.</Styled.Content>
          </Styled.ContentContainer>
          <PostCountInfo likeCount={likeCount} commentCount={commentCount} ariaHidden={true} />
        </Styled.BlockedContainer>
      );
    }
    return (
      <Styled.Container onClick={handleClick} ref={ref} data-testid={testid} tabIndex={0}>
        <Styled.HeadContainer>
          <Styled.TitleContainer isModified={modified}>
            <Styled.Title>{title}</Styled.Title>
            {modified && <Styled.Modified>(편집됨)</Styled.Modified>}
          </Styled.TitleContainer>
          <Styled.Date>{timeConverter(createdAt)}</Styled.Date>
        </Styled.HeadContainer>
        <Styled.ContentContainer>
          <Styled.Content>{content}</Styled.Content>
        </Styled.ContentContainer>
        <PostCountInfo likeCount={likeCount} commentCount={commentCount} ariaHidden={false} />
      </Styled.Container>
    );
  },
);

export default PostListItem;
