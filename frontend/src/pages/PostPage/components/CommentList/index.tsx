import { Fragment, useState } from 'react';

import CommentBox from '@/pages/PostPage/components/CommentBox';

import useComments from '@/hooks/queries/comment/useComments';

import * as Styled from './index.styles';

import CommentInput from '../CommentInput';

interface CommentListProps {
  id: string;
  boardId: number;
}

const CommentList = ({ id, boardId }: CommentListProps) => {
  const { data } = useComments({ storeCode: id });
  const [openedFormId, setOpenedFormId] = useState<undefined | number>(undefined);

  return (
    <>
      {data && (
        <Styled.Container>
          <CommentInput amount={data.totalCount} id={id} boardId={boardId} />
          <Styled.CommentsContainer>
            {data.comments.map(comment => (
              <Fragment key={comment.id}>
                <CommentBox
                  {...comment}
                  openedFormId={openedFormId}
                  setOpenedFormId={setOpenedFormId}
                  boardId={boardId}
                />
                {comment.replies.map(reply => (
                  <Styled.ReplyBox key={reply.id} mode="replies" {...reply} boardId={boardId} />
                ))}
              </Fragment>
            ))}
          </Styled.CommentsContainer>
        </Styled.Container>
      )}
    </>
  );
};

export default CommentList;
