import { useMutation, UseMutationOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import { createEmailCheck } from '@/api/member';
import type { CreateEmailCheckRequest } from '@/api/member';

const useEmailCheck = (
  options?: UseMutationOptions<AxiosResponse<null>, AxiosError<Error>, CreateEmailCheckRequest>,
) => {
  return useMutation(
    ({ email }): Promise<AxiosResponse<null>> =>
      createEmailCheck({
        email,
      }),
    {
      ...options,
    },
  );
};

export default useEmailCheck;
