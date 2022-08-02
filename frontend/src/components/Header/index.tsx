import { useContext, useReducer } from 'react';

import SearchModal from '@/components/SearchModal';

import AuthContext from '@/context/Auth';

import * as Styled from './index.styles';

import DropDownCaret from '@/assets/images/dropdown-caret.svg';
import Logo from '@/assets/images/logo.svg';
import PATH from '@/constants/path';

const Header = () => {
  const { isLogin, username } = useContext(AuthContext);
  const [isSearchModalOpen, handleSearchModal] = useReducer(state => !state, false);

  return (
    <Styled.Container>
      <Styled.LeftSide to={PATH.HOME}>
        <Logo width={35} height={35} />
        <Styled.Title>속닥속닥</Styled.Title>
      </Styled.LeftSide>
      <Styled.RightSide>
        <Styled.Search onClick={handleSearchModal} />
        {isLogin ? (
          <>
            <Styled.Avartar>{username[0]}</Styled.Avartar>
            <DropDownCaret />
          </>
        ) : (
          <Styled.LoginLink to={PATH.LOGIN}>로그인</Styled.LoginLink>
        )}
      </Styled.RightSide>
      {isSearchModalOpen && <SearchModal handleSearchModal={handleSearchModal} />}
    </Styled.Container>
  );
};

export default Header;
