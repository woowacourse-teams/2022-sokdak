import * as Styled from './index.styles';
import timeConverter from '@/utils/timeConverter';

export interface PostListItemProp {
  title: string;
  localDate: {
    date: string;
    time: string;
  };
  content: string;
}

const PostListItem = ({ title, localDate, content }: PostListItemProp) => {
  return (
    <Styled.Container>
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
