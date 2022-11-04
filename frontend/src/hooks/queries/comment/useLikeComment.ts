import { useMutation, UseMutationOptions, useQueryClient } from 'react-query';

import { AxiosResponse } from 'axios';

import { requestPutLikeComment } from '@/api/comment';
import type { GetCommentResponse, PutLikeCommentResponse } from '@/api/comment';
import QUERY_KEYS from '@/constants/queries';

interface UseLikeCommentProps {
  id: number;
}

const useLikeComment = (
  options?: UseMutationOptions<
    PutLikeCommentResponse,
    AxiosResponse<Error>,
    UseLikeCommentProps,
    PutLikeCommentResponse
  >,
) => {
  const queryClient = useQueryClient();

  return useMutation(({ id }) => requestPutLikeComment(String(id)), {
    ...options,
    onSuccess: (_, variables) => {
      queryClient.setQueriesData<GetCommentResponse>(QUERY_KEYS.COMMENTS, comment => {
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
        return newData as GetCommentResponse;
      });
    },
  });
};

export default useLikeComment;
