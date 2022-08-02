import CommentBox from '@/components/CommentBox';

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
          <CommentBox
            key={comment.id}
            authorized={comment.authorized}
            nickname={comment.nickname}
            content={comment.content}
            createdAt={comment.createdAt}
          />
        ))}
      </Styled.CommentsContainer>
    </Styled.Container>
  );
};

export default CommentList;
