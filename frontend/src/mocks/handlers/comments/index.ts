import { rest } from 'msw';

import { commentList, postList, reportCommentList } from '@/dummy';

const commentHandlers = [
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
      nickname: anonymous ? '짜증난 파이썬' : '테스트 계정',
      postId: id,
      authorized: true,
      blocked: false,
      postWriter: targetPost.authorized,
      replies: [],
    });
    targetPost.commentCount += 1;

    return res(ctx.status(204));
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

  rest.post<{ content: string; anonymous: boolean }>('/comments/:id/reply', (req, res, ctx) => {
    const id = Number(req.params.id);
    const { content, anonymous } = req.body;

    if (content.length < 1 || content.length > 255) {
      return res(ctx.status(400), ctx.json({ message: '대댓글은 1자 이상 255자 이하입니다.' }));
    }

    const targetPost = postList.find(post => post.id === id);

    if (!targetPost) {
      return res(ctx.status(400), ctx.json({ message: '해당 글이 존재하지 않습니다.' }));
    }

    targetPost.commentCount += 1;

    const targetCommentReplies = commentList.find(comment => comment.id === id)?.replies!;

    targetCommentReplies.push({
      id: targetPost.commentCount,
      nickname: anonymous ? '짜증난 파이썬' : '테스트 계정',
      content,
      createdAt: new Date().toISOString(),
      authorized: true,
      blocked: false,
      postWriter: targetPost.authorized,
    });

    return res(ctx.status(201));
  }),
];

export default commentHandlers;
