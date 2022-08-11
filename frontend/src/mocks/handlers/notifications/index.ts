import { rest } from 'msw';

import { notificationList } from '@/dummy';

const notificationHandlers = [
  rest.get('/notifications', (req, res, ctx) => {
    const size = Number(req.url.searchParams.get('size'));
    const page = Number(req.url.searchParams.get('page'));
    notificationList.sort((prev, next) => Number(new Date(next.createdAt)) - Number(new Date(prev.createdAt)));
    const notificationListByPage = notificationList.slice(page * size, size * (page + 1));
    notificationList.forEach((notification, index) => {
      if (index < page * size || index >= size * (page + 1)) return;
      notification.isChecked = true;
    });

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
    return res(ctx.status(200), ctx.json({ existence: notificationList.some(({ isChecked }) => !isChecked) }));
  }),

  rest.delete('/notifications/:id', (req, res, ctx) => {
    const { id } = req.params;

    const targetIndex = notificationList.findIndex(notification => notification.id === Number(id));

    if (targetIndex === -1) {
      return res(ctx.status(400), ctx.json({ message: '해당 알림이 존재하지 않습니다.' }));
    }
    notificationList.splice(targetIndex, 1);
    return res(ctx.status(204));
  }),
];

export default notificationHandlers;
