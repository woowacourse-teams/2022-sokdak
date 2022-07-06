import Layout from '@/components/@styled/Layout';
import * as Styled from './index.styles';

const PostPage = () => {
  return (
    <Layout>
      <Styled.Container>
        <Styled.TitleContainer>
          <Styled.Title>속닥속닥</Styled.Title>
          <Styled.Date>2022년 6월 22일</Styled.Date>
        </Styled.TitleContainer>
        <Styled.ContentContainer>
          <Styled.Content>
            안녕하세요.안녕하세요.안녕하세요.안녕하세요.안녕하세요.안녕하세요.안녕하세요.안녕하세요.안녕하세요.안녕하세요.안녕하세요.안녕하세요.안녕하세요.안녕하세요.안녕하세요.안녕하세요.안녕하세요.안녕하세요.안녕하세요.안녕하세요.
          </Styled.Content>
        </Styled.ContentContainer>
        <Styled.ListButtonContainer>
          <Styled.ListButton to="/">글 목록</Styled.ListButton>
        </Styled.ListButtonContainer>
      </Styled.Container>
    </Layout>
  );
};

export default PostPage;
