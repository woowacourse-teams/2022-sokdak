import { useId, useRef, useState } from 'react';

import usePostComments from '@/hooks/queries/comment/usePostComments';
import useSnackbar from '@/hooks/useSnackbar';

import * as Styled from './index.styles';

import SNACKBAR_MESSAGE from '@/constants/snackbar';

interface CommentInputProps {
  amount: number;
  id: string;
}

const CommentInput = ({ amount = 0, id }: CommentInputProps) => {
  const checkboxId = useId();
  const contentElement = useRef<HTMLTextAreaElement>(null);
  const formElement = useRef<HTMLFormElement>(null);
  const [anonymous, setAnonymous] = useState(true);
  const { showSnackbar } = useSnackbar();

  const { mutate } = usePostComments({
    onSuccess: () => {
      formElement.current?.reset();
      document.body.scrollIntoView({ behavior: 'smooth', block: 'end' });
    },
    onError: data => {
      showSnackbar(data.response?.data.message!);
    },
  });

  const handlePostComment = (e: React.FormEvent) => {
    e.preventDefault();
    if (!contentElement.current?.value) {
      contentElement.current?.focus();
      return showSnackbar(SNACKBAR_MESSAGE.FAIL_COMMENT);
    }
    mutate({ id, content: contentElement.current?.value, anonymous });
  };

  return (
    <Styled.Form onSubmit={handlePostComment} ref={formElement}>
      <Styled.CommentCount>{amount}개의 댓글</Styled.CommentCount>
      <Styled.ContentInput placeholder="댓글을 작성하세요." ref={contentElement} required />
      <Styled.CheckBoxContainer>
        <Styled.CheckBox
          type="checkbox"
          id={checkboxId}
          checked={anonymous}
          onChange={e => {
            setAnonymous(e.currentTarget.checked);
          }}
        />
        <Styled.Label htmlFor={checkboxId}>익명</Styled.Label>
      </Styled.CheckBoxContainer>
      <Styled.SubmitButton>댓글 작성</Styled.SubmitButton>
    </Styled.Form>
  );
};

export default CommentInput;
