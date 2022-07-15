import { rest } from 'msw';

import { memberList } from '@/dummy';

const memberHandler = [
  rest.post<Member>('/login', (req, res, ctx) => {
    const { username, password } = req.body;

    const targetMember = memberList.find(member => member.username === username);
    if (!targetMember) {
      return res(
        ctx.status(400),
        ctx.json({
          message: '해당 아이디를 가진 유저는 존재하지 않습니다.',
        }),
      );
    }
    if (targetMember.password !== password) {
      return res(ctx.status(400), ctx.json({ message: '잘못된 비밀번호 입니다.' }));
    }

    return res(ctx.status(200), ctx.cookie('JSESSIONID', 'FDB5E30BF20045E8A9AAFC788383680C'));
  }),
];

export default memberHandler;
