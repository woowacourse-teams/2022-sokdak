import { AxiosResponse } from 'axios';

const extractDataFromAxios = <T = unknown, D = unknown>(callback: Promise<AxiosResponse<T, D>>) =>
  callback.then(res => res.data);

export default extractDataFromAxios;
