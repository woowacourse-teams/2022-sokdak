import { AxiosResponse } from 'axios';

import authFetcher from './fetcher/auth';
import extractDataFromAxios from './util/extractor';

interface CommentList extends CommentType {
  blocked: boolean;
  postWriter: boolean;
  replies: Omit<CommentList, 'replies'>[];
}
export interface GetCommentResponse {
  comments: CommentList[];
  totalCount: number;
}

export const requestGetComment = (id: string) =>
  extractDataFromAxios<GetCommentResponse>(authFetcher.get(`/posts/${id}/comments`));

export interface CreateCommentsRequest {
  content: string;
  anonymous: boolean;
}

export const createComment = (id: string, body: CreateCommentsRequest) =>
  extractDataFromAxios<null, CreateCommentsRequest>(authFetcher.post(`posts/${id}/comments`, body));

export const requestDeleteComment = (id: string) =>
  extractDataFromAxios<null, null>(authFetcher.delete<null, AxiosResponse<null>, null>(`/comments/${id}`));

export interface PutLikeCommentResponse {
  like: boolean;
  likeCount: number;
}

export const requestPutLikeComment = (id: string) =>
  extractDataFromAxios<PutLikeCommentResponse>(authFetcher.put(`/comments/${id}/like`));

export interface CreateReportCommentRequest {
  message: string;
}

export const createReportComment = (id: string, body: CreateReportCommentRequest) =>
  extractDataFromAxios<null, CreateReportCommentRequest>(authFetcher.post(`/comments/${id}/report`, body));

export interface CreateReplyRequest {
  content: string;
  anonymous: boolean;
}

export const createReply = (id: string, body: CreateReplyRequest) =>
  extractDataFromAxios<null, CreateReplyRequest>(authFetcher.post(`comments/${id}/reply`, body));
