import { useId } from 'react';

import * as Styled from './index.styles';

const ComponentInput = () => {
  const id = useId();
  return (
    <Styled.Form>
      <Styled.CommentCount>3개의 댓글</Styled.CommentCount>
      <Styled.ContentInput placeholder="댓글을 작성하세요." required />
      <Styled.CheckBoxContainer>
        <Styled.CheckBox type="checkbox" id={id} />
        <Styled.Label htmlFor={id}>익명</Styled.Label>
      </Styled.CheckBoxContainer>
      <Styled.SubmitButton>댓글 작성</Styled.SubmitButton>
    </Styled.Form>
  );
};

export default ComponentInput;
