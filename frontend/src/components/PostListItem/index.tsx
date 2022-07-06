import * as Styled from './index.styles';
import timeConverter from '@/utils/timeConverter';
import { MouseEventHandler } from 'react';

export interface PostListItemProp extends Omit<Post, 'id'> {
  handleClick: (e: React.MouseEvent<HTMLDivElement, MouseEvent>) => void;
}

const PostListItem = ({ title, localDate, content, handleClick }: PostListItemProp) => {
  return (
    <Styled.Container onClick={handleClick}>
      <Styled.TitleContainer>
        <Styled.Title>{title}</Styled.Title>
        <Styled.Date>{timeConverter(localDate)}</Styled.Date>
      </Styled.TitleContainer>
      <Styled.ContentContainer>
        <Styled.Content>{content}</Styled.Content>
      </Styled.ContentContainer>
    </Styled.Container>
  );
};

export default PostListItem;
