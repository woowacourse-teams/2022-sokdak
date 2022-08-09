import { Fragment } from 'react';

import CommentBox from '@/pages/PostPage/components/CommentBox';

import useComments from '@/hooks/queries/comment/useComments';

import * as Styled from './index.styles';

import CommentInput from '../CommentInput';

interface CommentListProps {
  id: string;
}

const CommentList = ({ id }: CommentListProps) => {
  const { data } = useComments({ storeCode: id });

  return (
    <Styled.Container>
      <CommentInput amount={data?.length!} id={id} />
      <Styled.CommentsContainer>
        {data?.map(comment => (
          <Fragment key={comment.id}>
            <CommentBox {...comment} />
            {comment.replies.map(reply => (
              <Styled.ReplyBox key={reply.id} mode="replies" {...reply} />
            ))}
          </Fragment>
        ))}
      </Styled.CommentsContainer>
    </Styled.Container>
  );
};

export default CommentList;
