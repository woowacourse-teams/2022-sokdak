import { AxiosResponse } from 'axios';

import fetcher from './fetcher';
import authFetcher from './fetcher/auth';
import extractDataFromAxios from './util/extractor';

export const requestGetPost = (id: string) => extractDataFromAxios<Post>(authFetcher.get(`/posts/${id}`));

export interface GetPostsResponse {
  posts: Post[];
  lastPage: boolean;
}

export const requestGetPosts = (boardId: string, size: number, pageParam: string) =>
  extractDataFromAxios<GetPostsResponse>(fetcher.get(`/boards/${boardId}/posts?size=${size}&page=${pageParam}`));

export interface CreateNewPostRequest extends Pick<Post, 'title' | 'content' | 'imageName'> {
  anonymous?: boolean;
  hashtags: string[];
}

export const createNewPost = (boardId: string | number, body: CreateNewPostRequest) =>
  extractDataFromAxios<null, CreateNewPostRequest>(authFetcher.post(`/boards/${boardId}/posts`, body));

export const requestDeletePost = (id: string) => extractDataFromAxios<null>(authFetcher.delete(`/posts/${id}`));

export interface GetPostsByBoardsResponse {
  boards: {
    id: number;
    title: string;
    posts: Pick<Post, 'id' | 'likeCount' | 'commentCount' | 'title'>[];
  }[];
}

export const requestGetPostsByBoards = () =>
  extractDataFromAxios<GetPostsByBoardsResponse>(fetcher.get('/boards/contents')).then(data => data.boards);

export interface GetPostsByHashtagsResponse {
  posts: Post[];
  lastPage: boolean;
}

export const requestGetPostsByHashtags = (hashtagName: string, size: number, pageParam: string) =>
  extractDataFromAxios<GetPostsByHashtagsResponse>(
    fetcher.get(`/posts?hashtag=${hashtagName}&size=${size}&page=${pageParam}`),
  );

export interface CreatePostReportRequest {
  message: string;
}

export const createPostReport = (id: number, body: CreatePostReportRequest) =>
  extractDataFromAxios<null, CreatePostReportRequest>(authFetcher.post(`/posts/${id}/report`, body));

export interface GetSearchPostCountResponse {
  totalPostCount: number;
}

export const requestGetSearchPostCount = (query: string) =>
  extractDataFromAxios<GetSearchPostCountResponse>(
    fetcher.get(`/posts/count?query=${query.replaceAll(' ', '%7C').replaceAll('|', '%7C').replaceAll('+', '%7C')}`),
  ).then(data => data.totalPostCount);

export interface GetSearchPostsResponse {
  posts: Post[];
  lastPage: boolean;
}

export const requestGetSearchPosts = (query: string, size: number, pageParam: number) =>
  extractDataFromAxios<GetSearchPostsResponse>(
    fetcher.get(
      `/posts?query=${query
        .replaceAll(' ', '%7C')
        .replaceAll('|', '%7C')
        .replaceAll('+', '%7C')}&size=${size}&page=${pageParam}`,
    ),
  );

export interface UpdatePostRequest extends Pick<Post, 'title' | 'content' | 'imageName'> {
  hashtags: string[];
}

export const requestUpdatePost = (id: string, body: UpdatePostRequest) =>
  extractDataFromAxios<null, UpdatePostRequest>(authFetcher.put(`/posts/${id}`, body));

export interface CreateImageResponse {
  imageName: string;
}

export const createImage = (image: FormData) =>
  authFetcher.post<CreateImageResponse, AxiosResponse<CreateImageResponse>, FormData>(
    process.env.IMAGE_API_URL!,
    image,
  );

export interface UpdateLikePostResponse {
  like: boolean;
  likeCount: number;
}

export const requestUpdateLikePost = (id: string) =>
  extractDataFromAxios<UpdateLikePostResponse>(authFetcher.put(`/posts/${id}/like`));

export interface GetMyPostResponse {
  posts: Post[];
  totalPageCount: number;
}

export const requestGetMyPost = (size: number, page: number) =>
  extractDataFromAxios<GetMyPostResponse>(authFetcher.get(`/posts/me?size=${size}&page=${page - 1}`));
