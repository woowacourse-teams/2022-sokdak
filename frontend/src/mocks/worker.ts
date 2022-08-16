import { setupWorker } from 'msw';

import commentHandlers from './handlers/comments';
import hashtagsHandlers from './handlers/hashtags';
import memberHandler from './handlers/members';
import notificationHandlers from './handlers/notifications';
import postHandlers from './handlers/posts';

export const worker = setupWorker(
  ...postHandlers,
  ...memberHandler,
  ...hashtagsHandlers,
  ...commentHandlers,
  ...notificationHandlers,
);
