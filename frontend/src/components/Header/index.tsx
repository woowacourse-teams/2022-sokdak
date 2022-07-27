import { useContext } from 'react';

import AuthContext from '@/context/Auth';

import * as Styled from './index.styles';

import DropDownCaret from '@/assets/images/dropdown-caret.svg';
import Logo from '@/assets/images/logo.svg';
import PATH from '@/constants/path';

const Header = () => {
  const { isLogin, username } = useContext(AuthContext);
  return (
    <Styled.Container>
      <Styled.LeftSide to={PATH.HOME}>
        <Logo width={35} height={35} />
        <Styled.Title>속닥속닥</Styled.Title>
      </Styled.LeftSide>
      <Styled.RightSide>
        {isLogin ? (
          <>
            <Styled.Avartar>{username[0]}</Styled.Avartar>
            <DropDownCaret />
          </>
        ) : (
          <Styled.LoginLink to={PATH.LOGIN}>로그인</Styled.LoginLink>
        )}
      </Styled.RightSide>
    </Styled.Container>
  );
};

export default Header;
