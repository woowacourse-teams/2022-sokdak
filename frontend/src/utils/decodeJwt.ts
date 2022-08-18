function b64DecodeUnicode(str: string) {
  return decodeURIComponent(
    atob(str).replace(/(.)/g, function (m, p) {
      let code = p.charCodeAt(0).toString(16).toUpperCase();
      if (code.length < 2) {
        code = '0' + code;
      }
      return '%' + code;
    }),
  );
}

function decode(str: string) {
  let output = str.replace(/-/g, '+').replace(/_/g, '/');
  switch (output.length % 4) {
    case 0:
      break;
    case 2:
      output += '==';
      break;
    case 3:
      output += '=';
      break;
    default:
      throw 'Illegal base64url string!';
  }

  try {
    return b64DecodeUnicode(output);
  } catch (err) {
    return atob(output);
  }
}

export function parseJwt(token: string) {
  if (typeof token !== 'string') {
    throw new Error('Invalid token specified');
  }
  try {
    return JSON.parse(decode(token.split('.')[1]));
  } catch (e) {
    throw new Error('Invalid token specified: ' + e);
  }
}

export const isExpired = (data: { exp: number; iat: number }) => {
  return new Date(data.exp * 1000) < new Date();
};
