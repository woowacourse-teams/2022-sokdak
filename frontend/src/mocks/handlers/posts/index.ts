import { rest } from 'msw';

const postHandlers = [
  rest.post('/posts', (req, res, ctx) => {}),
  rest.get('/posts/:id', (req, res, ctx) => {}),
  rest.get('/posts', (req, res, ctx) => {}),
];

export default postHandlers;
