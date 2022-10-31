import { useMutation, UseMutationOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import useSnackbar from '@/hooks/useSnackbar';

import { createImage } from '@/api/post';
import { MUTATION_KEY } from '@/constants/queries';
import SNACKBAR_MESSAGE from '@/constants/snackbar';

interface ResponseData {
  imageName: string;
}

const useUploadImage = (options?: UseMutationOptions<AxiosResponse<ResponseData>, AxiosError, FormData>) => {
  const { showSnackbar } = useSnackbar();

  return useMutation(image => createImage(image), {
    ...options,
    onSuccess: (data, variables, context) => {
      showSnackbar(SNACKBAR_MESSAGE.SUCCESS_UPLOAD_IMAGE);

      if (options && options.onSuccess) {
        options.onSuccess(data, variables, context);
      }
    },
    onError: (error, variables, context) => {
      showSnackbar(SNACKBAR_MESSAGE.FAIL_UPLOAD_IMAGE);

      if (options && options.onError) {
        options.onError(error, variables, context);
      }
    },
    mutationKey: MUTATION_KEY.UPLOAD_IMAGE,
  });
};

export default useUploadImage;
