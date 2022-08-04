export const parseJwt = (token: string): { exp: number; iat: number; nickname?: string } | null => {
  try {
    return JSON.parse(atob(token.split('.')[1]));
  } catch (e) {
    return null;
  }
};

export const isExpired = (data: { exp: number; iat: number }) => {
  return new Date(data.exp * 1000) < new Date();
};
