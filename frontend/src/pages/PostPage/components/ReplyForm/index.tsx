import { useState } from 'react';

import CheckBox from '@/components/CheckBox';

import usePostReply from '@/hooks/queries/comment/usePostReply';

import * as Styled from './index.styles';

interface ReplyFormProps {
  commentId: string | number;
}

const ReplyForm = ({ commentId }: ReplyFormProps) => {
  const [content, setContent] = useState('');
  const [anonymous, setAnonymous] = useState(true);

  const { mutate: postReply } = usePostReply({});

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    postReply({ commentId, content, anonymous });
  };

  return (
    <Styled.Form onSubmit={handleSubmit}>
      <Styled.Input value={content} onChange={e => setContent(e.target.value)} placeholder="댓글을 작성하세요." />
      <Styled.Controller>
        <CheckBox isChecked={anonymous} setIsChecked={setAnonymous} labelText="익명" />
        <Styled.SubmitButton>작성</Styled.SubmitButton>
      </Styled.Controller>
    </Styled.Form>
  );
};

export default ReplyForm;
