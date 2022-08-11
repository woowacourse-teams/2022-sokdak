import { rest } from 'msw';

import { notificationList } from '@/dummy';

const notificationHandlers = [
  rest.get('/notifications', (req, res, ctx) => {
    const size = Number(req.url.searchParams.get('size'));
    const page = Number(req.url.searchParams.get('page'));
    notificationList.sort((prev, next) => Number(new Date(next.createdAt)) - Number(new Date(prev.createdAt)));
    const notificationListByPage = notificationList.slice(page * size, size * (page + 1));

    return res(
      ctx.status(200),
      ctx.json({
        notifications: notificationListByPage,
        lastPage:
          notificationList.length - size * page - notificationListByPage.length === 0 && notificationList.length !== 0,
      }),
    );
  }),
  rest.get('/notifications/check', (req, res, ctx) => {
    return res(ctx.status(200), ctx.json({ existence: true }));
  }),
];

export default notificationHandlers;
