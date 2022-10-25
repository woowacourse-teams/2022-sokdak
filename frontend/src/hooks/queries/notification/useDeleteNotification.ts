import { useMutation, UseMutationOptions, useQueryClient } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import authFetcher from '@/apis/authFetcher';
import { SIZE } from '@/constants/api';
import QUERY_KEYS, { MUTATION_KEY } from '@/constants/queries';

interface DeleteNotificationProps {
  id: number;
}

const useDeleteNotification = (
  options?: UseMutationOptions<AxiosResponse<never>, AxiosError<{ message: string }>, DeleteNotificationProps>,
) => {
  const queryClient = useQueryClient();

  return useMutation(({ id }) => authFetcher.delete(`/notifications/${id}`), {
    ...options,
    onSuccess: () => {
      queryClient.refetchQueries([QUERY_KEYS.NOTIFICATIONS, SIZE.NOTIFICATION_LOAD]);
    },
    mutationKey: MUTATION_KEY.DELETE_NOTIFICATION,
  });
};

export default useDeleteNotification;
