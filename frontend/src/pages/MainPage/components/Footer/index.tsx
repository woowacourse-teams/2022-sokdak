import Divider from '@/components/@styled/Divider';

import * as Styled from './index.styles';

const Footer = () => {
  return (
    <Styled.Container>
      <Styled.DividerContainer>
        <Divider horizontal={true} />
        <Styled.FooterItemContainer>
          <Styled.FooterItem href="https://github.com/woowacourse-teams/2022-sokdak" target="_blank">
            GitHub
          </Styled.FooterItem>
          |
          <Styled.FooterItem href="https://sokdak-sokdak.tistory.com/" target="_blank">
            Tech Blog
          </Styled.FooterItem>
        </Styled.FooterItemContainer>
      </Styled.DividerContainer>
    </Styled.Container>
  );
};

export default Footer;
