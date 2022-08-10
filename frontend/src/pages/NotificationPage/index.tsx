import NotificationItem from './components/NotificationItem';

import * as Styled from './index.styles';

import { convertToTimeType } from '@/utils/timeConverter';

const response: { notifications: Notice[] } = {
  notifications: [
    {
      id: 0,
      content: '알림 메시지입니다.',
      createdAt: '2022-08-10 16:04',
      type: 'NEW_COMMENT',
      postId: 1,
    },
    {
      id: 1,
      content: '알림 메시지입니다.',
      createdAt: '2022-08-08 16:04',
      type: 'NEW_COMMENT',
      postId: 1,
    },
    {
      id: 2,
      content: '새로운 대댓글',
      createdAt: '2022-08-07 16:02',
      type: 'NEW_REPLY',
      postId: 1,
    },
    {
      id: 3,
      content: '새로운 댓글 신고',
      createdAt: '2021-08-08 16:02',
      type: 'COMMENT_REPORT',
      postId: 1,
    },
    {
      id: 4,
      content: '댓글',
      createdAt: '2022-09-08 16:02',
      type: 'REPLY_COMMENT',
      postId: 1,
    },
    {
      id: 5,
      content: '게시글',
      createdAt: '2022-08-06 16:02',
      type: 'HOT_BOARD',
      postId: 1,
    },
    {
      id: 6,
      content: '알림 메시지입니다.',
      createdAt: '2022-08-09 16:04',
      type: 'NEW_COMMENT',
      postId: 1,
    },
  ],
};

const classifyByCreatedAt = (notifications: Notice[]) => {
  const obj: Record<TimeType, Notice[]> = {
    오늘: [],
    어제: [],
    이번주: [],
    이번달: [],
    오래전: [],
  };

  notifications.forEach(notification => {
    const createdAt = new Date(notification.createdAt);
    obj[convertToTimeType(createdAt)].push(notification);
  });

  return obj;
};

const NotificationPage = () => {
  const notificationObject = classifyByCreatedAt(response.notifications);

  const timeTypeArr: Array<TimeType> = [...new Set<TimeType>(['오늘', '어제', '이번주', '이번달', '오래전'])];

  return (
    <Styled.NotificationPageContainer>
      <Styled.Title>알람</Styled.Title>
      {timeTypeArr.map(type => {
        if (!Object.keys(notificationObject).includes(type)) {
          return <></>;
        }

        if (notificationObject[type].length <= 0) {
          return;
        }

        return (
          <Styled.NotificationContainer key={type}>
            <Styled.NotificationTime>{type}</Styled.NotificationTime>
            <Styled.NotificationItemContainer>
              {notificationObject[type].map(notification => (
                <NotificationItem key={notification.id} notification={notification} />
              ))}
            </Styled.NotificationItemContainer>
          </Styled.NotificationContainer>
        );
      })}
    </Styled.NotificationPageContainer>
  );
};

export default NotificationPage;
