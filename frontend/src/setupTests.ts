import { setupServer } from 'msw/node';

import memberHandler from './mocks/handlers/members';
import postHandlers from './mocks/handlers/posts';
import MockIntersectionObserver from '@/__test__/fixture';
import '@testing-library/jest-dom';
import { cleanup } from '@testing-library/react';

const server = setupServer(...memberHandler, ...postHandlers);

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
