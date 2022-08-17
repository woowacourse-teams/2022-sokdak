import PostCountInfo from '@/components/PostCountInfo';

import * as Styled from './index.styles';

import PATH from '@/constants/path';

interface BoardItemProps {
  id: number;
  title: string;
  posts: Pick<Post, 'likeCount' | 'commentCount' | 'title' | 'id'>[];
}

const BoardItem = ({ id, title, posts }: BoardItemProps) => {
  return (
    <div>
      <Styled.Title>{title}</Styled.Title>
      <Styled.ItemContainer>
        {posts.map(post => (
          <Styled.Item key={post.id} to={`${PATH.POST}/${post.id}`} data-testid={post.id}>
            <Styled.ItemTitle>{post.title}</Styled.ItemTitle>
            <PostCountInfo likeCount={post.likeCount} commentCount={post.commentCount} />
          </Styled.Item>
        ))}
      </Styled.ItemContainer>
      <Styled.LoadMoreButton to={`${PATH.BOARD}/${id}`}>더보기</Styled.LoadMoreButton>
    </div>
  );
};

export default BoardItem;
