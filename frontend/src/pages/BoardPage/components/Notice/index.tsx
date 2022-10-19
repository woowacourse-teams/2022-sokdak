import * as Styled from './index.styles';

const Notice = () => {
  return (
    <Styled.Container>
      <Styled.Text>
        현재 예비 크루 게시판은 <Styled.Highlight>우아한테크코스 공식 서비스가 아니며,</Styled.Highlight>
        <br />
        우아한테크코스 4기 크루들의 프로젝트 중 하나입니다.
        <br />
        게시된 글과 댓글은 우아한테크코스의 공식 입장이 아님을 주의 바랍니다.
        <br />
      </Styled.Text>
      <Styled.LinkContainer>
        <Styled.Link href="https://woowacourse.github.io/contact.html" target="_blank">
          우아한테크코스 공식 문의하기
        </Styled.Link>
        <Styled.Link href="https://woowacourse.github.io/faq.html" target="_blank">
          우아한테크코스 FAQ
        </Styled.Link>
      </Styled.LinkContainer>
    </Styled.Container>
  );
};

export default Notice;
