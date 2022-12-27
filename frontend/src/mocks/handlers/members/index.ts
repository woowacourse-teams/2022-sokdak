import { rest } from 'msw';

import { memberList, validMemberEmail } from '@/dummy';

interface SignUpProps extends Member {
  email: string;
  nickname: string;
  code: string;
  passwordConfirmation: string;
}

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

    return res(
      ctx.status(200),
      ctx.set({
        Authorization:
          'Bearer eyJhbGciOiJIUzUxMiJ9.eyJpZCI6MSwicm9sZSI6IkFETUlOIiwibmlja25hbWUiOiJURVNUIiwiaWF0IjoxNjY3ODAwMTQyLCJleHAiOjI2NjgzMDM3NDJ9.XEv2IcfS3to3GbMFcBciLc1pZpj39sOyMv6e9PT0-eq0pStz5Ok9ZsFp7AwGs2SFIthS9oGgyMCHbyQqZr5-NQ',
        'Refresh-Token':
          'Bearer eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE2NTg5MDIzOTcsImV4cCI6MjY2MDExMjM5N30.HCWdK3pVqdt6QZIQGvoNNwmnBGy6MRr1e-Pm3SWp6yXwSds3mpvtJjmtuHMa1bfsP0kQLR1TnLIKsclbe0Nedw',
      }),
    );
  }),

  rest.post<{ email: string }>('/members/signup/email', (req, res, ctx) => {
    const { email } = req.body;

    const targetEmail = validMemberEmail.find(member => member.email === email);

    if (!targetEmail) {
      return res(ctx.status(400), ctx.json({ message: '우아한테크코스 크루가 아닙니다.' }));
    }

    if (targetEmail.isSignedUp) {
      return res(ctx.status(400), ctx.json({ message: '이미 가입된 크루입니다.' }));
    }

    return res(ctx.status(204));
  }),

  rest.post<{ email: string; code: string }>('/members/signup/email/verification', (req, res, ctx) => {
    const { email, code } = req.body;

    const targetEmail = validMemberEmail.find(member => member.email === email);

    if (!targetEmail) {
      return res(ctx.status(400), ctx.json({ message: '우아한테크코스 크루가 아닙니다.' }));
    }

    if (targetEmail.isSignedUp) {
      return res(ctx.status(400), ctx.json({ message: '이미 가입된 크루입니다.' }));
    }

    if (targetEmail.code !== code) {
      return res(ctx.status(400), ctx.json({ message: '잘못된 인증번호입니다.' }));
    }

    return res(ctx.status(204));
  }),
  rest.get('/members/signup/exists', (req, res, ctx) => {
    if (req.url.searchParams.has('username')) {
      const id = req.url.searchParams.get('username');

      const existedID = validMemberEmail.find(member => member.ID === id);

      if (existedID) {
        return res(ctx.status(200), ctx.json({ unique: false }));
      }
      return res(ctx.status(200), ctx.json({ unique: true }));
    }
    if (req.url.searchParams.has('nickname')) {
      const nickname = req.url.searchParams.get('nickname');

      const existedNickname = validMemberEmail.find(member => member.nickname === nickname);

      if (existedNickname) {
        return res(ctx.status(200), ctx.json({ unique: false }));
      }
      return res(ctx.status(200), ctx.json({ unique: true }));
    }
  }),

  rest.post<SignUpProps>('/members/signup', (req, res, ctx) => {
    const { email, code, nickname, password, passwordConfirmation, username } = req.body;
    if (password !== passwordConfirmation) {
      return res(ctx.status(400), ctx.json({ message: '입력된 비밀번호와 비밀번호 확인란이 일치하지 않습니다.' }));
    }
    const targetEmail = validMemberEmail.find(member => member.email === email);

    if (!targetEmail) {
      return res(ctx.status(400), ctx.json({ message: '우아한테크코스 크루가 아닙니다.' }));
    }

    if (targetEmail.isSignedUp) {
      return res(ctx.status(400), ctx.json({ message: '이미 가입된 크루입니다.' }));
    }

    if (targetEmail.code !== code) {
      return res(ctx.status(400), ctx.json({ message: '잘못된 인증번호입니다.' }));
    }
    const existedID = validMemberEmail.find(member => member.ID === username);

    if (existedID) {
      return res(ctx.status(400), ctx.json({ message: '이미 존재하는 아이디입니다.' }));
    }

    const existedNickname = validMemberEmail.find(member => member.nickname === nickname);

    if (existedNickname) {
      return res(ctx.status(400), ctx.json({ message: '이미 존재하는 닉네임입니다.' }));
    }

    memberList.push({ username, password });

    return res(ctx.status(201));
  }),

  rest.patch<{ nickname: string }>('/members/nickname', (req, res, ctx) => {
    const { nickname } = req.body;

    if (!/^[가-힣a-zA-Z]+$/.test(nickname)) {
      if (/[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/.test(nickname)) {
        return res(ctx.status(400), ctx.json({ message: '닉네임에는 특수문자가 포함될 수 없습니다.' }));
      }

      if (/\s/.test(nickname)) {
        return res(ctx.status(400), ctx.json({ message: '닉네임에는 공백이 포함될 수 없습니다.' }));
      }

      return res(ctx.status(400), ctx.json({ message: '닉네임은 완전한 음절로만 작성해주세요.' }));
    }

    const existedNickname = validMemberEmail.find(member => member.nickname === nickname);

    if (existedNickname) {
      return res(ctx.status(400), ctx.json({ message: '이미 존재하는 닉네임입니다.' }));
    }

    return res(ctx.status(204));
  }),

  rest.get('/logout', (req, res, ctx) => {
    return res(ctx.status(204));
  }),
];

export default memberHandler;
