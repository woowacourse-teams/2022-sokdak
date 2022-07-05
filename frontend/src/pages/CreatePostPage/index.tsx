import Layout from '@/components/@styled/Layout';
import SnackbarContext from '@/context/Snackbar';
import { useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import * as Styled from './index.styles';

const CreatePostPage = () => {
  const navigate = useNavigate();
  const { showSnackbar } = useContext(SnackbarContext);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    showSnackbar('글 작성에 성공하였습니다.');
    navigate('/');
  };

  return (
    <Layout>
      <Styled.PostForm onSubmit={handleSubmit}>
        <Styled.Heading>글 작성</Styled.Heading>
        <Styled.TitleInput placeholder="제목을 입력해주세요." />
        <Styled.ContentInput placeholder="내용을 작성해주세요." />
        <Styled.PostButton>글 작성하기</Styled.PostButton>
      </Styled.PostForm>
    </Layout>
  );
};

export default CreatePostPage;
