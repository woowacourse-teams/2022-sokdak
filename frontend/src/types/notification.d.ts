interface Notice {
  id: number;
  content: string | null;
  createdAt: string;
  type: NotificationType;
  postId: number;
}

type NotificationType = 'NEW_COMMENT' | 'NEW_REPLY' | 'COMMENT_REPORT' | 'REPLY_COMMENT' | 'HOT_BOARD' | 'POST_REPORT';

type TimeType = '오늘' | '어제' | '이번주' | '이번달' | '오래전';
