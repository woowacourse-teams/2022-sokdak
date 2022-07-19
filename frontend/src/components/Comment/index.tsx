import * as Styled from './index.styles';

import timeConverter from '@/utils/timeConverter';

const Comment = ({ nickname, content, createdAt }: Comment) => {
  return (
    <Styled.Container>
      <Styled.Nickname>{nickname}</Styled.Nickname>
      <Styled.Content>{content}</Styled.Content>
      <Styled.Date>{timeConverter(createdAt)}</Styled.Date>
    </Styled.Container>
  );
};

export default Comment;
