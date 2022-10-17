import { useRef, useState } from 'react';

import CheckBox from '@/components/CheckBox';

import usePostComments from '@/hooks/queries/comment/usePostComments';
import useSnackbar from '@/hooks/useSnackbar';

import * as Styled from './index.styles';

import SNACKBAR_MESSAGE from '@/constants/snackbar';
import scrollToCurrent from '@/utils/scrollToCurrent';

interface CommentInputProps {
  amount: number;
  id: string;
  boardId: number;
}

const CommentInput = ({ amount = 0, id, boardId }: CommentInputProps) => {
  const contentElement = useRef<HTMLTextAreaElement>(null);
  const formElement = useRef<HTMLFormElement>(null);
  const [anonymous, setAnonymous] = useState(true);
  const { showSnackbar } = useSnackbar();

  const { mutate } = usePostComments({
    onSuccess: () => {
      formElement.current?.reset();
      scrollToCurrent(document.body.scrollHeight - document.documentElement.scrollTop);
    },
  });

  const handlePostComment = (e: React.FormEvent) => {
    e.preventDefault();
    if (!contentElement.current?.value) {
      contentElement.current?.focus();
      return showSnackbar(SNACKBAR_MESSAGE.FAIL_COMMENT);
    }
    mutate({ id, content: contentElement.current?.value, anonymous, boardId });
  };

  return (
    <Styled.Form onSubmit={handlePostComment} ref={formElement}>
      <Styled.CommentCount>{amount}개의 댓글</Styled.CommentCount>
      <Styled.ContentInput placeholder="댓글을 작성하세요." ref={contentElement} required />
      <CheckBox isChecked={anonymous} setIsChecked={setAnonymous} labelText="익명" />
      <Styled.SubmitButton>댓글 작성</Styled.SubmitButton>
    </Styled.Form>
  );
};

export default CommentInput;
