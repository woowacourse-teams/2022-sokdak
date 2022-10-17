import { useMutation, UseMutationOptions, useQueryClient } from 'react-query';

import { AxiosResponse } from 'axios';

import authFetcher from '@/apis';
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

const useLikeComment = (
  options?: UseMutationOptions<
    AxiosResponse<{ like: boolean; likeCount: number }>,
    AxiosResponse<Error>,
    { id: number }
  >,
) => {
  const queryClient = useQueryClient();

  return useMutation(({ id }) => authFetcher.put(`/comments/${id}/like`), {
    ...options,
    onSuccess: (_, variables) => {
      queryClient.setQueriesData<AxiosResponse<CommentResponse>>(QUERY_KEYS.COMMENTS, comment => {
        const newData = {
          ...comment,
          data: {
            totalCount: comment?.data.totalCount!,
            comments: comment?.data.comments.map(comment => {
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
        return newData as AxiosResponse<CommentResponse>;
      });
    },
  });
};

export default useLikeComment;
