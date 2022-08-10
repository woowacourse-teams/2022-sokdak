import { forwardRef, ReactNode } from 'react';

import * as Styled from './index.styles';

import PATH from '@/constants/path';

interface NotificationItemProps {
  notification: Notice;
}

const NotificationItem = forwardRef<HTMLAnchorElement, NotificationItemProps>(
  ({ notification }: NotificationItemProps, ref) => {
    if (!Object.keys(NotificationByTypes).includes(notification.type)) {
      return <>error</>;
    }

    return (
      <Styled.ItemContainer to={`${PATH.POST}/${notification.postId}`} ref={ref}>
        {NotificationByTypes[notification.type](notification)}
        <Styled.DeleteButton>x</Styled.DeleteButton>
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
