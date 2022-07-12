import * as Styled from './index.styles';
import timeConverter from '@/utils/timeConverter';
import { forwardRef } from 'react';

interface PostListItemProps extends Omit<Post, 'id'> {
  handleClick: (e: React.MouseEvent<HTMLDivElement, MouseEvent>) => void;
}

const PostListItem = forwardRef<HTMLDivElement, PostListItemProps>(
  ({ title, localDate, content, handleClick }: PostListItemProps, ref) => {
    return (
      <Styled.Container onClick={handleClick} ref={ref}>
        <Styled.TitleContainer>
          <Styled.Title>{title}</Styled.Title>
          <Styled.Date>{timeConverter(localDate)}</Styled.Date>
        </Styled.TitleContainer>
        <Styled.ContentContainer>
          <Styled.Content>{content}</Styled.Content>
        </Styled.ContentContainer>
      </Styled.Container>
    );
  },
);

export default PostListItem;
