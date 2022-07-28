import { MEMBER } from '@/constants/signUp';

export const isValidEmail = (email: string) =>
  /^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/.test(
    email,
  );

export const isValidID = (id: string) =>
  new RegExp(`^[a-zA-Z0-9]{${MEMBER.LIMIT.ID.MINIMUM_LENGTH},${MEMBER.LIMIT.ID.MAXIMUM_LENGTH}}$`).test(id);

export const isValidNickname = (id: string) =>
  new RegExp(
    `^[a-zA-Z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣]{${MEMBER.LIMIT.NICKNAME.MINIMUM_LENGTH},${MEMBER.LIMIT.NICKNAME.MAXIMUM_LENGTH}}$`,
  ).test(id);

export const isValidPassword = (password: string) =>
  /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,20}$/.test(password);
