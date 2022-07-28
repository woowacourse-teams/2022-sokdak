import { rest } from 'msw';

import { hashtagList, commentList, postList } from '@/dummy';

const postHandlers = [
  rest.post<Pick<Post, 'title' | 'content'> & { hashtags: string[] }>('/posts', (req, res, ctx) => {
    const { title, content, hashtags } = req.body;
    const id = postList.length + 1;

    if (!title || !content) {
      return res(ctx.status(400), ctx.json({ message: '제목 혹은 본문이 없습니다.' }));
    }

    hashtags.forEach(hashtagName => {
      if (hashtagList.some(hashtag => hashtag.name === hashtagName)) return;

      hashtagList.push({
        id: hashtagList.length + 1,
        name: hashtagName,
      });
    });

    const newPost: Post = {
      id,
      title,
      content,
      createdAt: new Date().toISOString(),
      likeCount: 0,
      commentCount: 0,
      modified: false,
      like: false,
      hashtags: hashtags.map(hashtagName => hashtagList.find(hashtag => hashtag.name === hashtagName)!),
      authorized: true,
    };

    postList.unshift(newPost);

    return res(ctx.status(200), ctx.set('Location', `/posts/${id}`));
  }),

  rest.put('/posts/:id/like', (req, res, ctx) => {
    const id = Number(req.params.id!);
    const targetPost = postList.find(post => post.id === id);

    if (!targetPost) {
      return res(
        ctx.status(400),
        ctx.json({
          message: '해당 포스트가 없습니다.',
        }),
      );
    }
    targetPost.like = !targetPost.like;
    if (targetPost.like) {
      targetPost.likeCount += 1;
    }
    if (!targetPost.like) {
      targetPost.likeCount -= 1;
    }
    return res(
      ctx.status(200),
      ctx.json({
        likeCount: targetPost.likeCount,
        like: targetPost.like,
      }),
    );
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

  rest.put<Pick<Post, 'title' | 'content'> & { hashtags: string[] }>('/posts/:id', (req, res, ctx) => {
    const params = req.params;
    const id = Number(params.id);
    const { title, content, hashtags } = req.body;

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

    hashtags.forEach(hashtagName => {
      if (hashtagList.some(hashtag => hashtag.name === hashtagName)) return;

      hashtagList.push({
        id: hashtagList.length + 1,
        name: hashtagName,
      });
    });

    targetPost.hashtags = hashtags.map(hashtagName => hashtagList.find(hashtag => hashtag.name === hashtagName)!);
    targetPost.modified = true;

    return res(ctx.status(204));
  }),

  rest.delete('/posts/:id', (req, res, ctx) => {
    const params = req.params;
    const id = Number(params.id);

    const isTargetPostExist = postList.some(post => post.id === id);

    if (!isTargetPostExist) {
      return res(ctx.status(400), ctx.json({ message: '해당 글이 존재하지 않습니다.' }));
    }

    postList.splice(
      postList.findIndex(post => post.id === id),
      1,
    );

    return res(ctx.status(204));
  }),
  rest.get('/posts/:id/comments', (req, res, ctx) => {
    const params = req.params;
    const id = Number(params.id);

    const targetCommentList = commentList.filter(comment => comment.postId === id);
    return res(ctx.status(200), ctx.json({ comments: targetCommentList }));
  }),

  rest.post<{ content: string; anonymous: boolean }>('/posts/:id/comments', (req, res, ctx) => {
    const params = req.params;
    const id = Number(params.id);
    const { content, anonymous } = req.body;

    const targetPost = postList.find(post => post.id === id);

    if (!targetPost) {
      return res(ctx.status(400), ctx.json({ message: '해당 글이 존재하지 않습니다.' }));
    }

    commentList.push({
      id: commentList.length + 1,
      content,
      createdAt: new Date().toISOString(),
      nickname: anonymous ? '익명' : '기명',
      postId: id,
    });
    targetPost.commentCount += 1;

    return res(ctx.status(204));
  }),
];

export default postHandlers;
