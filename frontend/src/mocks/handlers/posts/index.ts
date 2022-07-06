import { rest } from 'msw';
import { postList } from '@/dummy';

const postHandlers = [
  rest.post<Pick<Post, 'title' | 'content'>>('/posts', (req, res, ctx) => {
    const { title, content } = req.body;
    const id = postList.length + 1;
    const newPost: Post = {
      id,
      localDate: {
        date: '2022-07-01',
        time: '16:32',
      },
      title,
      content,
    };

    postList.push(newPost);

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
    const prevPage = page - 1;
    const posts = postList.slice(prevPage * size, prevPage * size + size);

    return res(
      ctx.status(200),
      ctx.json({
        posts,
        isLastPage: postList.length - size * prevPage - posts.length === 0 && posts.length !== 0,
      }),
    );
  }),
];

export default postHandlers;
