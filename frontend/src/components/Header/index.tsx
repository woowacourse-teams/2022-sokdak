import * as Styled from './index.styles';

import DropDownCaret from '@/assets/images/dropdown-caret.svg';
import Logo from '@/assets/images/logo.svg';
import PATH from '@/constants/path';

const Header = () => {
  return (
    <Styled.Container>
      <Styled.LeftSide to={PATH.HOME}>
        <Logo width={35} height={35} />
        <Styled.Title>속닥속닥</Styled.Title>
      </Styled.LeftSide>
      <Styled.RightSide>
        <Styled.Avartar />
        <DropDownCaret />
      </Styled.RightSide>
    </Styled.Container>
  );
};

export default Header;
