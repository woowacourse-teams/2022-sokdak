import { AxiosResponse } from 'axios';

import authFetcher from './authFetcher';

interface CommentList extends CommentType {
  blocked: boolean;
  postWriter: boolean;
  replies: Omit<CommentList, 'replies'>[];
}
export interface GetCommentResponse {
  comments: CommentList[];
  totalCount: number;
}

export const requestGetComment = async (id: string) => {
  const { data } = await authFetcher.get<GetCommentResponse>(`/posts/${id}/comments`);

  return data;
};

export interface CreateCommentsRequest {
  content: string;
  anonymous: boolean;
}

export const createComment = (id: string, body: CreateCommentsRequest) =>
  authFetcher.post<null, AxiosResponse<null>, CreateCommentsRequest>(`posts/${id}/comments`, body);

export const requestDeleteComment = (id: string) =>
  authFetcher.delete<null, AxiosResponse<null>, null>(`/comments/${id}`);

export interface PutLikeCommentResponse {
  like: boolean;
  likeCount: number;
}

export const requestPutLikeComment = async (id: string) => {
  const { data } = await authFetcher.put<PutLikeCommentResponse>(`/comments/${id}/like`);

  return data;
};

export interface CreateReportCommentRequest {
  message: string;
}

export const createReportComment = (id: string, body: CreateReportCommentRequest) =>
  authFetcher.post<null, AxiosResponse<null>, CreateReportCommentRequest>(`/comments/${id}/report`, body);

export interface CreateReplyRequest {
  content: string;
  anonymous: boolean;
}

export const createReply = (id: string, body: CreateReplyRequest) =>
  authFetcher.post<null, AxiosResponse<null>, CreateReplyRequest>(`comments/${id}/reply`, body);
