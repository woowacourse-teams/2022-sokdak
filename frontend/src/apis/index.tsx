import axios, { AxiosError } from 'axios';

import { STORAGE_KEY } from '@/constants/localStorage';
import { isExpired, parseJwt } from '@/utils/decodeJwt';

const authFetcher = axios.create();
authFetcher.defaults.baseURL = process.env.API_URL;

authFetcher.interceptors.request.use(
  async function (request) {
    const accessToken = authFetcher.defaults.headers.common['Authorization'] as string;
    if (accessToken && isExpired(parseJwt(accessToken)!)) {
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
        authFetcher.defaults.headers.common = {};
        localStorage.setItem(STORAGE_KEY.ACCESS_TOKEN, '');
        localStorage.setItem(STORAGE_KEY.REFRESH_TOKEN, '');
        throw new AxiosError('토큰이 만료되었습니다.');
      }
    }

    return request;
  },
  function (error: AxiosError) {
    console.log(error);
    return Promise.reject(error);
  },
);

export default authFetcher;
