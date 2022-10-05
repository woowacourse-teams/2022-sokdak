import Divider from '@/components/@styled/Divider';

import * as Styled from './index.styles';

const Footer = () => {
  return (
    <Styled.Container>
      <Styled.DividerContainer>
        <Divider horizontal={true} />
        <Styled.FooterItemContainer>
          <Styled.FooterItem>서비스 소개</Styled.FooterItem>|<Styled.FooterItem>이용약관</Styled.FooterItem>|
          <Styled.FooterItem>디렉토리</Styled.FooterItem>|<Styled.FooterItem>개인정보 처리방침</Styled.FooterItem>|
          <Styled.FooterItem>신고가이드</Styled.FooterItem>
        </Styled.FooterItemContainer>
      </Styled.DividerContainer>
    </Styled.Container>
  );
};

export default Footer;
