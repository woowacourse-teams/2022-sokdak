import { useMutation, UseMutationOptions } from 'react-query';

import { AxiosError } from 'axios';

import { createEmailCheck } from '@/api/member';
import type { CreateEmailCheckRequest } from '@/api/member';

const useEmailCheck = (options?: UseMutationOptions<null, AxiosError<Error>, CreateEmailCheckRequest>) => {
  return useMutation(
    ({ email }) =>
      createEmailCheck({
        email,
      }),
    {
      ...options,
    },
  );
};

export default useEmailCheck;
