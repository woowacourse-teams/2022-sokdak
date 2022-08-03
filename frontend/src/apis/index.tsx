import axios, { AxiosError } from 'axios';

import { isExpired, parseJwt } from '@/utils/decodeJwt';

const authFetcher = axios.create();
authFetcher.interceptors.request.use(
  async function (request) {
    const accessToken = axios.defaults.headers.common['Authorization'] as string;
    if (accessToken && isExpired(parseJwt(accessToken)!)) {
      const data = await axios.get<never>(`refresh`);
      axios.defaults.headers.common['Authorization'] = data.headers.authorization;
    }
    return request;
  },
  function (error: AxiosError) {
    return Promise.reject(error);
  },
);

export default authFetcher;
