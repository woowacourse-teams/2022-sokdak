import * as Styled from './index.styles';

import PATH from '@/constants/path';
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
          <Styled.Item key={post.id} to={`${PATH.POST}/${post.id}`} data-testid={post.id}>
            <Styled.ItemTitle>{post.title}</Styled.ItemTitle>
            <Styled.PostInfoContainer>
              <Styled.IconContainer>
                <Styled.LikeIcon />
                <Styled.LikeCount>{countFormatter(post.likeCount)}</Styled.LikeCount>
              </Styled.IconContainer>
              <Styled.IconContainer>
                <Styled.CommentIcon />
                <Styled.CommentCount>{countFormatter(post.commentCount)}</Styled.CommentCount>
              </Styled.IconContainer>
            </Styled.PostInfoContainer>
          </Styled.Item>
        ))}
      </Styled.ItemContainer>
      <Styled.LoadMoreButton to={`${PATH.BOARD}/${id}`}>더보기</Styled.LoadMoreButton>
    </div>
  );
};

export default BoardItem;
