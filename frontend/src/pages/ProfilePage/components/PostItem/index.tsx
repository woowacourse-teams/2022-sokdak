import PostCountInfo from '@/components/PostCountInfo';

import * as Styled from './index.styles';

import timeConverter from '@/utils/timeConverter';

interface PostItemProps {
  title: string;
  content: string;
  createdAt: string;
  likeCount: number;
  commentCount: number;
}

const PostItem = ({ title, content, createdAt, likeCount, commentCount }: PostItemProps) => {
  return (
    <Styled.Container>
      <Styled.Title>{title}</Styled.Title>
      <Styled.Content>{content}</Styled.Content>
      <Styled.Information>
        <Styled.CreatedAt>{timeConverter(createdAt)}</Styled.CreatedAt>
        <PostCountInfo likeCount={likeCount} commentCount={commentCount} />
      </Styled.Information>
    </Styled.Container>
  );
};

export default PostItem;
