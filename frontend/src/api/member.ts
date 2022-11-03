import { AxiosRequestConfig, AxiosResponse } from 'axios';

import fetcher from './fetcher';
import authFetcher from './fetcher/auth';

export interface CreateMemberRequest extends Member {
  email: string | null;
  nickname: string;
  code: string | null;
  passwordConfirmation: string;
}

export const createMember = (body: CreateMemberRequest) =>
  fetcher.post<null, AxiosResponse<null>, CreateMemberRequest>('/members/signup', body);

export interface CreateEmailCheckRequest {
  email: string;
}

export const createEmailCheck = (body: CreateEmailCheckRequest) =>
  fetcher.post<null, AxiosResponse<null>, CreateEmailCheckRequest>('/members/signup/email', body);

export interface CreateVerificationCodeCheckRequest {
  email: string;
  code: string;
}

export const createVerificationCodeCheck = (body: CreateVerificationCodeCheckRequest) =>
  fetcher.post<null, AxiosResponse<null>, CreateVerificationCodeCheckRequest>(
    '/members/signup/email/verification',
    body,
  );

export const createLogin = (body: Member, options: AxiosRequestConfig) =>
  fetcher.post<null, AxiosResponse<null>, Member>('/login', body, options);

export interface GetNicknameCheckResponse {
  unique: boolean;
}

export const requestGetNicknameCheck = async (nickname: string) => {
  const { data } = await fetcher.get<GetNicknameCheckResponse>(`members/signup/exists?nickname=${nickname}`);

  return data.unique;
};

export interface GetIDCheckResponse {
  unique: boolean;
}

export const requestGetIDCheck = async (username: string) => {
  const { data } = await fetcher.get<GetIDCheckResponse>(`members/signup/exists?username=${username}`);

  return data.unique;
};

export const requestGetLogout = () => authFetcher.get('/logout');

export interface UpdateNicknameRequest {
  nickname: string;
}
export const requestUpdateNickname = (body: UpdateNicknameRequest) =>
  authFetcher.patch<null, AxiosResponse<null>, UpdateNicknameRequest>('/members/nickname', body);
