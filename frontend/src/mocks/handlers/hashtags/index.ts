import { rest } from 'msw';

import { hashtagList } from '@/dummy';

const hashtagsHandlers = [
  rest.get('/api/hashtags/popular', (req, res, ctx) => {
    const limit = Number(req.url.searchParams.get('limit')!);
    const include = req.url.searchParams.get('include')!;

    const filteredHashtagList = hashtagList.filter(hashtag => hashtag.name.includes(include));
    const slicedHashtagList = filteredHashtagList.slice(0, limit);
    const hashtags = slicedHashtagList.sort((a, b) => b.count - a.count);

    return res(
      ctx.status(200),
      ctx.json({
        hashtags,
      }),
    );
  }),
];

export default hashtagsHandlers;
