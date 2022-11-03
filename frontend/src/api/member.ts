import { AxiosRequestConfig, AxiosResponse } from 'axios';

import fetcher from './fetcher';
import authFetcher from './fetcher/auth';
import extractDataFromAxios from './util/extractor';

export interface CreateMemberRequest extends Member {
  email: string | null;
  nickname: string;
  code: string | null;
  passwordConfirmation: string;
}

export const createMember = (body: CreateMemberRequest) =>
  extractDataFromAxios<null, CreateMemberRequest>(fetcher.post('/members/signup', body));

export interface CreateEmailCheckRequest {
  email: string;
}

export const createEmailCheck = (body: CreateEmailCheckRequest) =>
  extractDataFromAxios<null, CreateEmailCheckRequest>(fetcher.post('/members/signup/email', body));

export interface CreateVerificationCodeCheckRequest {
  email: string;
  code: string;
}

export const createVerificationCodeCheck = (body: CreateVerificationCodeCheckRequest) =>
  extractDataFromAxios<null, CreateVerificationCodeCheckRequest>(
    fetcher.post('/members/signup/email/verification', body),
  );

export const createLogin = (body: Member, options: AxiosRequestConfig) =>
  fetcher.post<null, AxiosResponse<null>, Member>('/login', body, options);

export interface GetNicknameCheckResponse {
  unique: boolean;
}

export const requestGetNicknameCheck = (nickname: string) =>
  extractDataFromAxios<GetNicknameCheckResponse>(fetcher.get(`members/signup/exists?nickname=${nickname}`)).then(
    data => data.unique,
  );

export interface GetIDCheckResponse {
  unique: boolean;
}

export const requestGetIDCheck = (username: string) =>
  extractDataFromAxios<GetIDCheckResponse>(fetcher.get(`members/signup/exists?username=${username}`)).then(
    data => data.unique,
  );

export const requestGetLogout = () => extractDataFromAxios<null>(authFetcher.get('/logout'));

export interface UpdateNicknameRequest {
  nickname: string;
}

export const requestUpdateNickname = (body: UpdateNicknameRequest) =>
  authFetcher.patch<null, AxiosResponse<null>, UpdateNicknameRequest>('/members/nickname', body);
