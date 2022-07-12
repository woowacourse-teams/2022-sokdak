import { setupWorker } from 'msw';

import postHandlers from './handlers/posts';

export const worker = setupWorker(...postHandlers);
