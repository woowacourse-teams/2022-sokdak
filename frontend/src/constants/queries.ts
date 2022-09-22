const QUERY_KEYS = {
  POST: 'post',
  POSTS: 'posts',
  MEMBER_ID_CHECK: 'member-id-check',
  MEMBER_NICKNAME_CHECK: 'member-nickname-check',
  MEMBER_REFRESH: 'member-refresh',
  COMMENTS: 'comments',
  POSTS_BY_BOARDS: 'posts-by-boards',
  HASHTAGS: 'hashtags',
  BOARDS: 'boards',
  LOGOUT: 'logout',
  NOTIFICATION_EXISTS: 'notification-exists',
  NOTIFICATIONS: 'notifications',
  MY_POSTS: 'my-posts',
};

export const MUTATION_KEY = {
  CREATE_POST: 'create-post',
  DELETE_POST: 'delete-post',
  REPORT_POST: 'report-post',
  UPDATE_POST: 'update-post',
  CREATE_LIKE: 'create-like',
  REPORT_COMMENT: 'report-comment',
  DELETE_COMMENT: 'delete-comment',
  POST_COMMENT: 'post-comment',
  CREATE_REPLY: 'create-reply',
  DELETE_NOTIFICATION: 'delete-notification',
  UPLOAD_IMAGE: 'upload-image',
};

export default QUERY_KEYS;
