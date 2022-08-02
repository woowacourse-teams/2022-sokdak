export const isValidEmail = (email: string) =>
  /^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/.test(
    email,
  );

export const isValidID = (id: string) => /^[a-zA-Z0-9]{4,16}$/.test(id);

export const isValidNickname = (id: string) => /^[a-zA-Z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣]{1,16}$/.test(id);

export const isValidPassword = (password: string) =>
  /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,20}$/.test(password);
