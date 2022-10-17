import { useNavigate } from 'react-router-dom';

import PostCountInfo from '@/components/PostCountInfo';

import * as Styled from './index.styles';

import timeConverter from '@/utils/timeConverter';

interface PostItemProps {
  id: number;
  title: string;
  content: string;
  createdAt: string;
  likeCount: number;
  commentCount: number;
}

const PostItem = ({ id, title, content, createdAt, likeCount, commentCount }: PostItemProps) => {
  const navagate = useNavigate();

  return (
    <Styled.Container onClick={() => navagate(`/post/${id}`)}>
      <Styled.Title>{title}</Styled.Title>
      <Styled.Content>{content}</Styled.Content>
      <Styled.Information>
        <Styled.CreatedAt>{timeConverter(createdAt)}</Styled.CreatedAt>
        <PostCountInfo likeCount={likeCount} commentCount={commentCount} ariaHidden={false} />
      </Styled.Information>
    </Styled.Container>
  );
};

export default PostItem;
