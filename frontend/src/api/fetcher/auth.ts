import axios, { AxiosError } from 'axios';

import { STORAGE_KEY } from '@/constants/localStorage';
import { isExpired, parseJwt } from '@/utils/decodeJwt';

const authFetcher = axios.create({
  baseURL: process.env.API_URL,
  withCredentials: true,
});

const accessToken = localStorage.getItem(STORAGE_KEY.ACCESS_TOKEN);
const refreshToken = localStorage.getItem(STORAGE_KEY.REFRESH_TOKEN);

if (refreshToken && isExpired(parseJwt(refreshToken))) {
  localStorage.removeItem(STORAGE_KEY.ACCESS_TOKEN);
  localStorage.removeItem(STORAGE_KEY.REFRESH_TOKEN);
}

if (accessToken) authFetcher.defaults.headers.common['Authorization'] = accessToken;

if (refreshToken && !isExpired(parseJwt(refreshToken))) {
  authFetcher.defaults.headers.common['Refresh-Token'] = refreshToken;
}

authFetcher.interceptors.request.use(
  async function (request) {
    const accessToken = authFetcher.defaults.headers.common['Authorization'] as string;

    if (accessToken && isExpired(parseJwt(accessToken))) {
      try {
        const data = await axios.get<never>(`/refresh`, {
          headers: {
            Authorization: authFetcher.defaults.headers.common['Authorization'],
            'Refresh-Token': authFetcher.defaults.headers.common['Refresh-Token'],
          },
        });
        if (request.headers) request.headers['Authorization'] = data.headers.authorization;
        authFetcher.defaults.headers.common['Authorization'] = data.headers.authorization;
      } catch (e) {
        delete authFetcher.defaults.headers.common['Authorization'];
        localStorage.removeItem(STORAGE_KEY.ACCESS_TOKEN);
        localStorage.removeItem(STORAGE_KEY.REFRESH_TOKEN);
        throw new AxiosError('토큰이 만료되었습니다.');
      }
    }

    return request;
  },
  function (error: AxiosError) {
    return Promise.reject(error);
  },
);

export default authFetcher;
