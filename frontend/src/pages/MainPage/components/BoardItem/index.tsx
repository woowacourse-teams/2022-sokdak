import * as Styled from './index.styles';

import countFormatter from '@/utils/countFormatter';

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
          <Styled.Item key={post.id} to={`/post/${post.id}`}>
            {post.title}
            <Styled.PostInfoContainer>
              <Styled.LikeIcon />
              <Styled.LikeCount>{countFormatter(post.likeCount)}</Styled.LikeCount>
              <Styled.CommentIcon />
              <Styled.CommentCount>{countFormatter(post.commentCount)}</Styled.CommentCount>
            </Styled.PostInfoContainer>
          </Styled.Item>
        ))}
      </Styled.ItemContainer>
      <Styled.LoadMoreButton to={`/board/${id}/posts`}>더보기</Styled.LoadMoreButton>
    </div>
  );
};

export default BoardItem;
