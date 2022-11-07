interface ErrorResponse {
  message: string;
}

interface AccessToken {
  id: number;
  role: string;
  nickname: string;
  iat: number;
  exp: number;
}
