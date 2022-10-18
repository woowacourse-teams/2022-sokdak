import { useContext, useReducer } from 'react';
import { useLocation } from 'react-router-dom';

import SearchModal from '@/components/SearchModal';

import AuthContext from '@/context/Auth';

import useLogout from '@/hooks/queries/member/useLogout';
import useResponsive from '@/hooks/useResponsive';

import * as Styled from './index.styles';

import DropDownCaret from '@/assets/images/dropdown-caret.svg';
import Logo from '@/assets/images/logo.svg';
import PATH from '@/constants/path';

import Dropdown from '../@shared/Dropdown';
import Divider from '../@styled/Divider';
import Notification from './Notification';
import SearchWeb from './SearchWeb';

const Header = () => {
  const { isLogin, username } = useContext(AuthContext);
  const [isSearchModalOpen, handleSearchModal] = useReducer(state => !state, false);
  const { refetch: logout } = useLogout();
  const isHeaderSizeLineOver = useResponsive(875);
  const isDesktopSizeLineOver = useResponsive(1140);
  const { pathname } = useLocation();

  const handleClickLogout = () => logout();

  if (pathname === PATH.LOGIN || pathname === PATH.SIGN_UP) return null;

  return (
    <>
      <Styled.Container>
        <Styled.LeftSide to={PATH.HOME}>
          <Logo width={35} height={35} />
          <Styled.Title>속닥속닥</Styled.Title>
        </Styled.LeftSide>
        {isHeaderSizeLineOver && <SearchWeb onClickSearchIcon={handleSearchModal} />}
        <Styled.RightSide>
          {!isHeaderSizeLineOver && <Styled.Search onClick={handleSearchModal} />}
          {isLogin && username ? (
            <>
              <Notification />
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

      {isDesktopSizeLineOver && <Divider horizontal />}
    </>
  );
};

export default Header;
