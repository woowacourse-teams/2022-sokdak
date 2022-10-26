import { AxiosRequestConfig } from 'axios';

import api from '.';
import authFetcher from './authFetcher';

export const createMember = (body: {
  email: string | null;
  username: string;
  nickname: string;
  code: string | null;
  password: string;
  passwordConfirmation: string;
}) => api.post('/members/signup', body);

export const createEmailCheck = (body: { email: string }) => api.post('/members/signup/email', body);

export const createVerificationCodeCheck = (body: { email: string; code: string }) =>
  api.post('/members/signup/email/verification', body);

export const createLogin = (body: { username: string; password: string }, options: AxiosRequestConfig) =>
  api.post('/login', body, options);

export const requestGetNicknameCheck = async (nickname: string) => {
  const { data } = await api.get(`members/signup/exists?nickname=${nickname}`);

  return data.unique;
};

export const requestGetIDCheck = async (username: string) => {
  const { data } = await api.get(`members/signup/exists?username=${username}`);

  return data.unique;
};

export const requestGetLogout = () => authFetcher.get('/logout');

export const requestUpdateNickname = (body: { nickname: string }) => authFetcher.patch('/members/nickname', body);
