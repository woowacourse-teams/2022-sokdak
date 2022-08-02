import * as Styled from './index.styles';

import timeConverter from '@/utils/timeConverter';

const CommentBox = ({ nickname, content, createdAt, authorized }: CommentType) => {
  return (
    <Styled.Container>
      <Styled.CommentHeader>
        <Styled.Nickname>{nickname}</Styled.Nickname>
        {authorized ? <Styled.ReportButton>🚨</Styled.ReportButton> : <Styled.DeleteButton>삭제</Styled.DeleteButton>}
      </Styled.CommentHeader>
      <Styled.Content>{content}</Styled.Content>
      <Styled.Date>{timeConverter(createdAt)}</Styled.Date>
    </Styled.Container>
  );
};

export default CommentBox;
