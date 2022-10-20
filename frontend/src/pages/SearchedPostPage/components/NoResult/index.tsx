import * as Styled from './index.styles';

const NoResult = () => {
  return (
    <Styled.Container>
      검색 결과가 존재하지 않습니다. <br />
      올바른 검색어를 입력했는지 확인해주세요. <br />
      <br />- 두 가지 단어를 함께 검색하는 경우 <Styled.Highlight>띄어쓰기</Styled.Highlight>를 해보세요. <br />
      <br />
      🔍 예시 | <Styled.Highlight>조시</Styled.Highlight>와 <Styled.Highlight>조현근</Styled.Highlight>을 함께 검색하는
      경우 <br />
      <br />
      검색어 = <Styled.Highlight>&apos;조시 조현근&apos;</Styled.Highlight> <br />
      조시, 조현근 관련 글이 모두 표시됩니다.
    </Styled.Container>
  );
};

export default NoResult;
