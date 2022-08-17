import { useEffect, useState } from 'react';

import CheckBox from '@/components/CheckBox';

import useCreateReply from '@/hooks/queries/comment/useCreateReply';
import useSnackbar from '@/hooks/useSnackbar';

import * as Styled from './index.styles';

import SCROLL from '@/constants/scroll';
import scrollToCurrent from '@/utils/scrollToCurrent';

interface ReplyFormProps {
  commentId: string | number;
}

const ReplyForm = ({ commentId }: ReplyFormProps) => {
  const { showSnackbar } = useSnackbar();

  const [content, setContent] = useState('');
  const [anonymous, setAnonymous] = useState(true);

  const { mutate: postReply } = useCreateReply({
    onSuccess: () => {
      setContent('');
      scrollToCurrent(SCROLL.COMMENT_HEIGHT);
    },
  });

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    postReply({ commentId, content, anonymous });
  };

  useEffect(() => {
    scrollToCurrent(SCROLL.REPLY_FORM_HEIGHT);
  }, []);

  return (
    <Styled.Form onSubmit={handleSubmit}>
      <Styled.Input
        value={content}
        onChange={e => setContent(e.target.value)}
        placeholder="댓글을 작성하세요."
        maxLength={255}
        onInvalid={(e: React.FormEvent<HTMLTextAreaElement> & { target: HTMLTextAreaElement }) => {
          e.preventDefault();
          showSnackbar(e.target.placeholder);
        }}
        required
      />
      <Styled.Controller>
        <CheckBox isChecked={anonymous} setIsChecked={setAnonymous} labelText="익명" />
        <Styled.SubmitButton>작성</Styled.SubmitButton>
      </Styled.Controller>
    </Styled.Form>
  );
};

export default ReplyForm;
