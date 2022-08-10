import { ReactNode } from 'react';

import * as Styled from './index.styles';

import PATH from '@/constants/path';

type NotificationType = 'NEW_COMMENT' | 'NEW_REPLY' | 'COMMENT_REPORT' | 'REPLY_COMMENT' | 'HOT_BOARD' | 'POST_REPORT';

interface NotificationItemProps {
  notification: {
    id: number;
    content: string | null;
    createdAt: string;
    type: NotificationType;
    postId: number;
  };
}

const NotificationItem = ({ notification }: NotificationItemProps) => {
  return (
    <Styled.ItemContainer to={`${PATH.POST}/${notification.postId}`}>
      {NotificationByTypes[notification.type](notification)}
      <Styled.DeleteButton>x</Styled.DeleteButton>
    </Styled.ItemContainer>
  );
};

export default NotificationItem;

const NotificationByTypes: Record<
  NotificationType,
  (notification: NotificationItemProps['notification']) => ReactNode
> = {
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
