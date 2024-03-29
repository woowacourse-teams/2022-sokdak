import { setupServer } from 'msw/node';

import { STORAGE_KEY } from './constants/localStorage';

import commentHandlers from './mocks/handlers/comments';
import hashtagsHandlers from './mocks/handlers/hashtags';
import memberHandler from './mocks/handlers/members';
import postHandlers from './mocks/handlers/posts';
import MockIntersectionObserver from '@/__test__/fixture';
import '@testing-library/jest-dom';
import { cleanup } from '@testing-library/react';

const server = setupServer(...memberHandler, ...postHandlers, ...hashtagsHandlers, ...commentHandlers);

const originalError = console.error;

beforeAll(() => {
  console.error = (...args) => {
    if (/Warning.*not wrapped in act/.test(args[0])) {
      return;
    }

    originalError.call(console, ...args);
  };
  server.listen();
  window.IntersectionObserver = MockIntersectionObserver;
});

afterAll(() => {
  console.error = originalError;

  server.close();
  jest.resetAllMocks();
});

afterEach(() => {
  cleanup();
  server.resetHandlers();
});

beforeEach(() => {
  localStorage.removeItem(STORAGE_KEY.ACCESS_TOKEN);
  localStorage.removeItem(STORAGE_KEY.REFRESH_TOKEN);
  // @ts-ignore
  globalThis.IS_REACT_ACT_ENVIRONMENT = true;
});

jest.mock('react-router-dom', () => {
  return {
    __esModule: true,
    ...jest.requireActual('react-router-dom'),
  };
});

window.HTMLElement.prototype.scrollIntoView = jest.fn();
