import { useMutation, UseMutationOptions } from 'react-query';

import axios, { AxiosError, AxiosResponse } from 'axios';

const useLike = (
  options?: UseMutationOptions<
    AxiosResponse<{ like: boolean; likeCount: number }, string>,
    AxiosError<{ message: string }>,
    { id: string }
  >,
) => {
  return useMutation(
    ({ id }): Promise<AxiosResponse<{ like: boolean; likeCount: number }, string>> => axios.put(`/posts/${id}/like`),
    options,
  );
};

export default useLike;
