import Layout from '@/components/@styled/Layout';
import * as Styled from './index.styles';

const CreatePostPage = () => {
  return (
    <Layout>
      <Styled.PostForm>
        <Styled.Heading>글 작성</Styled.Heading>
        <Styled.TitleInput placeholder="제목을 입력해주세요." />
        <Styled.ContentInput placeholder="내용을 작성해주세요." />
        <Styled.PostButton>글 작성하기</Styled.PostButton>
      </Styled.PostForm>
    </Layout>
  );
};

export default CreatePostPage;
