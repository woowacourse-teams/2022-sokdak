interface MemberConstant {
  LIMIT: Record<string, { MINIMUM_LENGTH: number; MAXIMUM_LENGTH: number }>;
}

export const MEMBER: MemberConstant = {
  LIMIT: {
    PASSWORD: {
      MINIMUM_LENGTH: 8,
      MAXIMUM_LENGTH: 20,
    },
    NICKNAME: {
      MINIMUM_LENGTH: 1,
      MAXIMUM_LENGTH: 16,
    },
    ID: {
      MINIMUM_LENGTH: 4,
      MAXIMUM_LENGTH: 16,
    },
  },
};

export const SIGN_UP_ERROR = {
  INVALID_PASSWORD: `비밀번호는 영어, 숫자, 특수문자가 포함된 ${MEMBER.LIMIT.PASSWORD.MINIMUM_LENGTH}~${MEMBER.LIMIT.PASSWORD.MAXIMUM_LENGTH}자입니다.`,
  BLANK_PASSWORD: '비밀번호를 입력해주세요.',
  INVALID_PASSWORD_CONFIRMATION: '비밀번호와 일치하지 않습니다.',
  DUPLICATED_NICKNAME: '중복된 닉네임입니다.',
  BLANK_NICKNAME: '닉네임을 입력해주세요.',
  INVALID_NICKNAME: `닉네임은 ${MEMBER.LIMIT.NICKNAME.MINIMUM_LENGTH}~${MEMBER.LIMIT.NICKNAME.MAXIMUM_LENGTH}자입니다.`,
  DUPLICATED_ID: '중복된 아이디입니다.',
  INVALID_ID: `아이디는 ${MEMBER.LIMIT.ID.MINIMUM_LENGTH}~${MEMBER.LIMIT.ID.MAXIMUM_LENGTH}자입니다.`,
  BLANK_ID: '아이디를 입력해주세요.',
  INVALID_EMAIL: '올바른 이메일 형식을 입력해주세요',
};
