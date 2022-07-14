import { rest } from 'msw';

import { postList } from '@/dummy';

const postHandlers = [
  rest.post<Pick<Post, 'title' | 'content'>>('/posts', (req, res, ctx) => {
    const { title, content } = req.body;
    const id = postList.length + 1;
    const newPost: Post = {
      id,
      title,
      content,
      createdAt: new Date().toISOString(),
    };

    postList.unshift(newPost);

    return res(ctx.status(200), ctx.set('Location', `/posts/${id}`));
  }),

  rest.get('/posts/:id', (req, res, ctx) => {
    const { id } = req.params;
    const targetPost = postList.find(post => post.id === Number(id));

    if (!targetPost) {
      return res(ctx.status(404), ctx.json({ message: '해당 글이 존재하지 않습니다.' }));
    }
    return res(ctx.status(200), ctx.json(targetPost));
  }),

  rest.get('/posts', (req, res, ctx) => {
    const size = Number(req.url.searchParams.get('size')!);
    const page = Number(req.url.searchParams.get('page')!);
    const posts = postList.slice(page * size, page * size + size);

    return res(
      ctx.status(200),
      ctx.json({
        posts,
        lastPage: postList.length - size * page - posts.length === 0 && posts.length !== 0,
      }),
    );
  }),

  rest.put<Pick<Post, 'title' | 'content'>>('/posts/:id', (req, res, ctx) => {
    const params = req.params;
    const id = Number(params.id);
    const { title, content } = req.body;

    const isTargetPostExist = postList.some(post => post.id === id);

    if (!isTargetPostExist) {
      return res(ctx.status(400), ctx.json({ message: '해당 글이 존재하지 않습니다.' }));
    }

    if (!title || !content) {
      return res(ctx.status(400), ctx.json({ message: '제목 혹은 본문이 없습니다.' }));
    }

    const targetPost = postList.find(post => post.id === id)!;

    targetPost.title = title;
    targetPost.content = content;

    return res(ctx.status(204));
  }),
];

export default postHandlers;
