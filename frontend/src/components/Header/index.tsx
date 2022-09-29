import { useContext, useEffect, useReducer, useState } from 'react';

import SearchModal from '@/components/SearchModal';

import AuthContext from '@/context/Auth';

import useLogout from '@/hooks/queries/member/useLogout';

import * as Styled from './index.styles';

import DropDownCaret from '@/assets/images/dropdown-caret.svg';
import Logo from '@/assets/images/logo.svg';
import PATH from '@/constants/path';
import throttle from '@/utils/throttle';

import Dropdown from '../@shared/Dropdown';
import Divider from '../@styled/Divider';
import Layout from '../@styled/Layout';
import SearchWeb from './SearchWeb';

const Header = () => {
  const { isLogin, username } = useContext(AuthContext);
  const [isSearchModalOpen, handleSearchModal] = useReducer(state => !state, false);
  const { refetch: logout } = useLogout();
  const [size, setSize] = useState<number>(window.innerWidth);

  const handleResize = throttle(() => {
    setSize(window.innerWidth);
  }, 100);

  useEffect(() => {
    window.addEventListener('resize', handleResize);
    return () => {
      window.removeEventListener('resize', handleResize);
    };
  }, []);

  const handleClickLogout = () => {
    logout();
  };

  return (
    <>
      <Layout>
        <Styled.Container>
          <Styled.LeftSide to={PATH.HOME}>
            <Logo width={35} height={35} />
            <Styled.Title>속닥속닥</Styled.Title>
          </Styled.LeftSide>
          <Styled.RightSide>
            {size > 875 ? (
              <SearchWeb onClickSearchIcon={handleSearchModal} />
            ) : (
              <Styled.Search onClick={handleSearchModal} />
            )}

            {isLogin && username ? (
              <>
                <Styled.DropdownContainer>
                  <Dropdown>
                    <Dropdown.Trigger>
                      <Styled.AvatarContainer>
                        <Styled.Panda />
                        <DropDownCaret />
                      </Styled.AvatarContainer>
                    </Dropdown.Trigger>
                    <Dropdown.OptionList>
                      <Styled.OptionsContainer>
                        <Styled.ProfileLink to={PATH.PROFILE}>프로필</Styled.ProfileLink>
                        <Styled.LogoutButton onClick={handleClickLogout}>로그아웃</Styled.LogoutButton>
                      </Styled.OptionsContainer>
                    </Dropdown.OptionList>
                  </Dropdown>
                </Styled.DropdownContainer>
              </>
            ) : (
              <Styled.LoginLink to={PATH.LOGIN}>로그인</Styled.LoginLink>
            )}
          </Styled.RightSide>
          {isSearchModalOpen && <SearchModal handleSearchModal={handleSearchModal} />}
        </Styled.Container>
      </Layout>
      {size > 1140 ? <Divider horizontal /> : <></>}
    </>
  );
};

export default Header;
