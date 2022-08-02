import { rest } from 'msw';

import { hashtagList, commentList, postList, boardList, reportList, reportCommentList } from '@/dummy';

const postHandlers = [
  rest.post<Pick<Post, 'title' | 'content'> & { hashtags: string[]; anonymous: boolean }>('/posts', (req, res, ctx) => {
    const { title, content, hashtags, anonymous } = req.body;
    const id = postList.length + 1;

    if (!title || !content) {
      return res(ctx.status(400), ctx.json({ message: '제목 혹은 본문이 없습니다.' }));
    }

    hashtags.forEach(hashtagName => {
      const existedTag = hashtagList.find(hashtag => hashtag.name === hashtagName);

      if (existedTag) {
        existedTag.count += 1;
        return;
      }

      hashtagList.push({
        id: hashtagList.length + 1,
        name: hashtagName,
        count: 1,
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
      boardId: 1,
      nickname: anonymous ? '짜증난 파이썬' : '테스트 계정',
      blocked: false,
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
      const existedTag = hashtagList.find(hashtag => hashtag.name === hashtagName);

      if (existedTag) {
        existedTag.count += 1;
        return;
      }

      hashtagList.push({
        id: hashtagList.length + 1,
        name: hashtagName,
        count: 1,
      });
    });

    targetPost.hashtags = hashtags.map(hashtagName => hashtagList.find(hashtag => hashtag.name === hashtagName)!);
    targetPost.modified = true;

    return res(ctx.status(204));
  }),

  rest.delete('/posts/:id', (req, res, ctx) => {
    const params = req.params;
    const id = Number(params.id);

    const targetPostIndex = postList.findIndex(post => post.id === id);
    const targetPost = postList[targetPostIndex];

    if (!targetPost) {
      return res(ctx.status(400), ctx.json({ message: '해당 글이 존재하지 않습니다.' }));
    }

    targetPost.hashtags.forEach(({ id }) => {
      const targetHashtagIndex = hashtagList.findIndex(tag => tag.id === id)!;
      const targetHashtag = hashtagList[targetHashtagIndex];

      if (targetHashtag.count <= 1) {
        hashtagList.splice(targetHashtagIndex, 1);

        return;
      }

      targetHashtag.count -= 1;
    });

    postList.splice(targetPostIndex, 1);

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
      authorized: true,
    });
    targetPost.commentCount += 1;

    return res(ctx.status(204));
  }),

  rest.get('/boards/contents', (req, res, ctx) => {
    const boards = boardList.map(board => {
      return {
        ...board,
        posts: postList
          .filter(({ boardId }) => board.id === boardId)
          .slice(0, 3)
          .map(({ id, likeCount, commentCount, title }) => ({ id, likeCount, commentCount, title })),
      };
    });
    return res(ctx.status(200), ctx.json({ boards: boards }));
  }),

  rest.get('/boards/:id/posts', (req, res, ctx) => {
    const params = req.params;
    const boardId = Number(params.id);

    const size = Number(req.url.searchParams.get('size')!);
    const page = Number(req.url.searchParams.get('page')!);
    const postsInBoard = postList.filter(post => post.boardId === boardId);
    const posts = postsInBoard.slice(page * size, page * size + size);

    return res(
      ctx.status(200),
      ctx.json({
        posts,
        lastPage: postsInBoard.length - size * page - posts.length === 0 && posts.length !== 0,
      }),
    );
  }),

  rest.get('/posts', (req, res, ctx) => {
    const hashtagName = req.url.searchParams.get('hashtag');
    const size = Number(req.url.searchParams.get('size')!);
    const page = Number(req.url.searchParams.get('page')!);

    const postsByHashtag = postList.filter(post => post.hashtags.some(hashtag => hashtag.name === hashtagName));

    if (postsByHashtag.length <= 0) {
      return res(ctx.status(404), ctx.json({ message: '해당 해시태그 관련 글이 존재하지 않습니다.' }));
    }

    const posts = postsByHashtag.slice(page * size, page * size + size);

    return res(
      ctx.status(200),
      ctx.json({
        posts,
        lastPage: postsByHashtag.length - size * page - posts.length === 0 && posts.length !== 0,
      }),
    );
  }),
  rest.post<{ message: string }>('/posts/:id/report', (req, res, ctx) => {
    const { id } = req.params;
    const { message } = req.body;

    const existedReport = reportList.find(({ postId }) => postId === Number(id));
    if (existedReport) {
      return res(ctx.status(400), ctx.json({ message: '이미 신고한 게시글입니다.' }));
    }

    const targetPost = postList.find(post => post.id === Number(id));
    if (!targetPost) {
      return res(ctx.status(400), ctx.json({ message: '존재하지 않는 게시글입니다.' }));
    }
    reportList.push({
      postId: Number(id),
      message,
    });
    return res(ctx.status(201));
  }),

  rest.post<{ message: string }>('/comments/:id/report', (req, res, ctx) => {
    const { id } = req.params;
    const { message } = req.body;
    if (reportCommentList.some(({ commentId }) => commentId === Number(id))) {
      return res(ctx.status(400), ctx.json({ message: '이미 신고한 댓글입니다.' }));
    }
    reportCommentList.push({ commentId: Number(id), message });
    return res(ctx.status(201));
  }),

  rest.delete('/comments/:id', (req, res, ctx) => {
    const { id } = req.params;
    const targetCommentIdx = commentList.findIndex(comment => comment.id === Number(id));

    if (targetCommentIdx === -1) {
      return res(ctx.status(400), ctx.json({ message: '해당 댓글이 존재하지 않습니다.' }));
    }
    commentList.splice(targetCommentIdx, 1);
    return res(ctx.status(204));
  }),
];

export default postHandlers;
