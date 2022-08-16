import useNotificationExists from '@/hooks/queries/notification/useNotificationExists';

import * as Styled from './index.styles';

const Notification = () => {
  const { data: isExists } = useNotificationExists({
    options: {
      refetchInterval: 30000,
    },
  });

  return (
    <Styled.NotificationContainer to={'/notification'}>
      {isExists && <Styled.AlarmPointer />}
      <Styled.NotificationIcon />
    </Styled.NotificationContainer>
  );
};

export default Notification;
