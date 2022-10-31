import { rest } from 'msw';

const pushHandlers = [
  rest.get('/subscription/key', (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json({
        key: '',
      }),
    );
  }),

  rest.post('/subscription', (req, res, ctx) => {
    return res(ctx.status(204));
  }),

  rest.delete('/subscription', (req, res, ctx) => {
    return res(ctx.status(204));
  }),
];

export default pushHandlers;
