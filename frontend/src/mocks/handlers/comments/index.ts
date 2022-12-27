import { rest } from 'msw';

import { commentList, commentListTable, postList, reportCommentList } from '@/dummy';

const commentHandlers = [
  rest.get('/api/posts/:id/comments', (req, res, ctx) => {
    const params = req.params;
    const id = Number(params.id);

    const targetCommentList = commentList.filter(comment => comment.postId === id);
    const totalCount =
      targetCommentList.filter(comment => comment.content).length +
      targetCommentList.flatMap(comment => comment.replies).length;

    return res(ctx.status(200), ctx.json({ comments: targetCommentList, totalCount }));
  }),

  rest.post<{ content: string; anonymous: boolean }>('/api/posts/:id/comments', (req, res, ctx) => {
    const params = req.params;
    const id = Number(params.id);
    const { content, anonymous } = req.body;

    const targetPost = postList.find(post => post.id === id);

    if (!targetPost) {
      return res(ctx.status(400), ctx.json({ message: '해당 글이 존재하지 않습니다.' }));
    }

    targetPost.commentCount += 1;
    commentListTable.id += 1;

    commentList.push({
      id: commentListTable.id,
      content,
      createdAt: new Date().toISOString(),
      nickname: anonymous ? '짜증난 파이썬' : '테스트 계정',
      postId: id,
      authorized: true,
      blocked: false,
      like: false,
      postWriter: targetPost.authorized,
      likeCount: 0,
      replies: [],
    });

    return res(ctx.status(204));
  }),

  rest.post<{ message: string }>('/api/comments/:id/report', (req, res, ctx) => {
    const id = Number(req.params.id);
    const { message } = req.body;

    if (reportCommentList.some(({ commentId }) => commentId === id)) {
      return res(ctx.status(400), ctx.json({ message: '이미 신고한 댓글입니다.' }));
    }
    reportCommentList.push({ commentId: id, message });

    return res(ctx.status(201));
  }),

  rest.delete('/api/comments/:id', (req, res, ctx) => {
    let isCommentExist = false;
    const id = Number(req.params.id);

    for (const comment of commentList) {
      if (comment.id === id && comment.replies.length > 0) {
        comment.nickname = null;
        comment.content = null;
        comment.createdAt = null;
        comment.authorized = null;
        comment.postWriter = null;
        comment.blocked = null;
        isCommentExist = true;
        postList.find(post => post.id === comment.postId)!.commentCount -= 1;

        break;
      }

      if (comment.id === id) {
        commentList.splice(commentList.indexOf(comment), 1);
        isCommentExist = true;
        postList.find(post => post.id === comment.postId)!.commentCount -= 1;

        break;
      }

      for (const reply of comment.replies) {
        if (reply.id === id) {
          comment.replies.splice(comment.replies.indexOf(reply), 1);
          isCommentExist = true;
          postList.find(post => post.id === comment.postId)!.commentCount -= 1;

          break;
        }
      }

      if (comment.replies.length <= 0 && !comment.content) {
        commentList.splice(commentList.indexOf(comment), 1);
      }
    }

    if (!isCommentExist) {
      return res(ctx.status(400), ctx.json({ message: '해당 댓글이 존재하지 않습니다.' }));
    }

    return res(ctx.status(204));
  }),

  rest.post<{ content: string; anonymous: boolean }>('/api/comments/:id/reply', (req, res, ctx) => {
    const id = Number(req.params.id);
    const { content, anonymous } = req.body;

    if (content.length < 1 || content.length > 255) {
      return res(ctx.status(400), ctx.json({ message: '대댓글은 1자 이상 255자 이하입니다.' }));
    }

    const parentComment = commentList.find(comment => comment.id === id)!;
    const targetPost = postList.find(post => post.id === parentComment.postId);

    if (!targetPost) {
      return res(ctx.status(400), ctx.json({ message: '해당 글이 존재하지 않습니다.' }));
    }

    targetPost.commentCount += 1;
    commentListTable.id += 1;

    const targetCommentReplies = commentList.find(comment => comment.id === id)?.replies!;

    targetCommentReplies.push({
      id: commentListTable.id,
      nickname: anonymous ? '짜증난 파이썬' : '테스트 계정',
      content,
      createdAt: new Date().toISOString(),
      authorized: true,
      blocked: false,
      likeCount: 0,
      like: false,
      postWriter: targetPost.authorized,
    });

    return res(ctx.status(201));
  }),

  rest.put('/api/comments/:id/like', (req, res, ctx) => {
    const { id } = req.params;
    const targetComment = commentList.find(comment => comment.id === Number(id));

    if (!targetComment) {
      const targetReply = commentList.flatMap(comment => comment.replies).find(reply => reply.id === Number(id));

      if (!targetReply) return res(ctx.status(400), ctx.json({ message: '존재하지 않는 댓글입니다.' }));

      targetReply.like = !targetReply.like;
      if (targetReply.like) {
        targetReply.likeCount += 1;
      }
      if (!targetReply.like) {
        targetReply.likeCount -= 1;
      }
      return res(
        ctx.status(200),
        ctx.json({
          like: targetReply.like,
          likeCount: targetReply.likeCount,
        }),
      );
    }

    targetComment.like = !targetComment.like;
    if (targetComment.like) {
      targetComment.likeCount += 1;
    }
    if (!targetComment.like) {
      targetComment.likeCount -= 1;
    }

    return res(
      ctx.status(200),
      ctx.json({
        like: targetComment.like,
        likeCount: targetComment.likeCount,
      }),
    );
  }),
];

export default commentHandlers;
