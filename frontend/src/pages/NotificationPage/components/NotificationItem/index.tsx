import React, { forwardRef, ReactNode } from 'react';
import { useNavigate } from 'react-router-dom';

import useDeleteNotification from '@/hooks/queries/notification/useDeleteNotification';

import * as Styled from './index.styles';

import PATH from '@/constants/path';

interface NotificationItemProps {
  notification: Notice;
}

const NotificationItem = forwardRef<HTMLDivElement, NotificationItemProps>(
  ({ notification }: NotificationItemProps, ref) => {
    if (!Object.keys(NotificationByTypes).includes(notification.type)) {
      return <>error</>;
    }

    const navigate = useNavigate();

    const { mutate: deleteNotification } = useDeleteNotification();

    const handleClickDeleteButton = (e: React.MouseEvent<HTMLButtonElement>) => {
      e.stopPropagation();
      deleteNotification({ id: notification.id });
    };

    return (
      <Styled.ItemContainer
        onClick={() => {
          navigate(`${PATH.POST}/${notification.postId}`);
        }}
        ref={ref}
      >
        {NotificationByTypes[notification.type](notification)}
        <Styled.DeleteButton onClick={handleClickDeleteButton}>x</Styled.DeleteButton>
      </Styled.ItemContainer>
    );
  },
);

export default NotificationItem;

const NotificationByTypes: Record<NotificationType, (notification: Notice) => ReactNode> = {
  NEW_COMMENT: notification => (
    <Styled.NotificationMessage>
      <Styled.Highlight>{notification.content}</Styled.Highlight> 에 댓글이 달렸습니다.
    </Styled.NotificationMessage>
  ),
  NEW_REPLY: notification => (
    <Styled.NotificationMessage>
      <Styled.Highlight>{notification.content}</Styled.Highlight> 댓글에 대댓글이 달렸습니다.
    </Styled.NotificationMessage>
  ),
  COMMENT_REPORT: notification => (
    <Styled.NotificationMessage>
      <Styled.Highlight>{notification.content}</Styled.Highlight> 댓글이 신고로 인해 블라인드 처리되었습니다.
    </Styled.NotificationMessage>
  ),
  HOT_BOARD: notification => (
    <Styled.NotificationMessage>
      <Styled.Highlight>{notification.content}</Styled.Highlight> 글이 <Styled.RedLight>HOT 게시판</Styled.RedLight>
      으로 선정되었습니다!
    </Styled.NotificationMessage>
  ),
  POST_REPORT: notification => (
    <Styled.NotificationMessage>
      <Styled.Highlight>{notification.content}</Styled.Highlight> 글이 신고로 인해 블라인드 처리되었습니다.
    </Styled.NotificationMessage>
  ),
  REPLY_COMMENT: notification => (
    <Styled.NotificationMessage>
      <Styled.Highlight>{notification.content}</Styled.Highlight>의 댓글이 신고로 인해 블라인드 처리되었습니다.
    </Styled.NotificationMessage>
  ),
};
