import { useEffect, useRef, useState } from 'react';

import CheckBox from '@/components/CheckBox';

import useCreateReply from '@/hooks/queries/comment/useCreateReply';
import useSnackbar from '@/hooks/useSnackbar';

import * as Styled from './index.styles';

import SCROLL from '@/constants/scroll';
import scrollToCurrent from '@/utils/scrollToCurrent';

interface ReplyFormProps {
  commentId: string | number;
  setIsReplyFormOpen: React.Dispatch<React.SetStateAction<boolean>>;
  boardId: number;
}

const ReplyForm = ({ commentId, setIsReplyFormOpen, boardId }: ReplyFormProps) => {
  const { showSnackbar } = useSnackbar();

  const [content, setContent] = useState('');
  const [anonymous, setAnonymous] = useState(true);
  const scrollRef = useRef<HTMLFormElement>(null);

  const { mutate: postReply } = useCreateReply({
    onSuccess: () => {
      setContent('');
      scrollToCurrent(SCROLL.COMMENT_HEIGHT);
    },
  });

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    postReply({ commentId, content, anonymous, boardId });
  };

  const handleClickCancel = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
    e.preventDefault();
    setIsReplyFormOpen(false);
  };

  useEffect(() => {
    scrollRef.current?.scrollIntoView({ behavior: 'smooth', block: 'center' });
  }, []);

  return (
    <Styled.Form onSubmit={handleSubmit} ref={scrollRef}>
      <Styled.Input
        value={content}
        onChange={e => setContent(e.target.value)}
        placeholder="댓글을 작성하세요."
        maxLength={255}
        onInvalid={(e: React.FormEvent<HTMLTextAreaElement> & { target: HTMLTextAreaElement }) => {
          e.preventDefault();
          showSnackbar(e.target.placeholder);
        }}
        autoFocus
        required
      />
      <Styled.Controller>
        <CheckBox isChecked={anonymous} setIsChecked={setAnonymous} labelText="익명" />
        <Styled.ButtonContainer>
          <Styled.CancelButton onClick={handleClickCancel}>취소</Styled.CancelButton>
          <Styled.SubmitButton>작성</Styled.SubmitButton>
        </Styled.ButtonContainer>
      </Styled.Controller>
    </Styled.Form>
  );
};

export default ReplyForm;
