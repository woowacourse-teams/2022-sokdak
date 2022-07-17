export const isValidEmail = (email: string) =>
  /^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/.test(
    email,
  );

export const isValidID = (id: string) => /^[a-zA-Z0-9]{3,16}$/.test(id);
