import { setupServer } from 'msw/node';

import memberHandler from './mocks/handlers/members';
import postHandlers from './mocks/handlers/posts';
import '@testing-library/jest-dom';

const server = setupServer(...memberHandler, ...postHandlers);

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

jest.mock('react-router-dom', () => {
  return {
    __esModule: true,
    ...jest.requireActual('react-router-dom'),
  };
});

window.HTMLElement.prototype.scrollIntoView = jest.fn();
