import { useMutation, UseMutationOptions, useQueryClient } from 'react-query';

import { AxiosResponse } from 'axios';

import { requestPutLikeComment } from '@/api/comment';
import QUERY_KEYS from '@/constants/queries';

interface CommentList extends CommentType {
  id: number;
  blocked: boolean;
  postWriter: boolean;
  replies: Omit<CommentList, 'replies'>[];
}

interface CommentResponse {
  comments: CommentList[];
  totalCount: number;
}
// export interface UseMutationOptions<TData = unknown, TError = unknown, TVariables = void, TContext = unknown> extends Omit<MutationObserverOptions<TData, TError, TVariables, TContext>, '_defaulted' | 'variables'> {

const useLikeComment = (
  options?: UseMutationOptions<
    { like: boolean; likeCount: number },
    AxiosResponse<Error>,
    { id: number },
    { like: boolean; likeCount: number }
  >,
) => {
  const queryClient = useQueryClient();

  return useMutation(({ id }) => requestPutLikeComment(String(id)), {
    ...options,
    onSuccess: (_, variables) => {
      queryClient.setQueriesData<CommentResponse>(QUERY_KEYS.COMMENTS, comment => {
        const newData = {
          ...comment,
          data: {
            totalCount: comment?.totalCount!,
            comments: comment?.comments.map(comment => {
              if (comment.id === variables.id) {
                comment.like = !comment.like;
                if (comment.like) comment.likeCount += 1;
                if (!comment.like) comment.likeCount -= 1;
              }
              const targetReply = comment.replies.find(reply => reply.id === variables.id);
              if (targetReply) {
                targetReply.like = !targetReply.like;
                if (targetReply.like) targetReply.likeCount += 1;
                if (!targetReply.like) targetReply.likeCount -= 1;
              }
              return comment;
            })!,
          },
        };
        return newData as CommentResponse;
      });
    },
  });
};

export default useLikeComment;
