import Layout from '@/components/@styled/Layout';
import Spinner from '@/components/Spinner';
import SnackbarContext from '@/context/Snackbar';
import axios from 'axios';
import { useContext, useState } from 'react';
import { useMutation, useQueryClient } from 'react-query';
import { useNavigate } from 'react-router-dom';
import * as Styled from './index.styles';

const CreatePostPage = () => {
  const queryClient = useQueryClient();
  const navigate = useNavigate();
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const { showSnackbar } = useContext(SnackbarContext);
  const createPost = () =>
    axios.post('/posts', {
      title,
      content,
    });

  const { mutate: registerPost, isLoading } = useMutation(createPost, {
    onSuccess: () => {
      queryClient.invalidateQueries();
      queryClient.invalidateQueries('posts-getByPage');
    },
  });

  if (isLoading) {
    return (
      <Layout>
        <Styled.SpinnerContainer>
          <Spinner />
        </Styled.SpinnerContainer>
      </Layout>
    );
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    registerPost();
    showSnackbar('글 작성에 성공하였습니다.');
    navigate('/');
  };

  return (
    <Layout>
      <Styled.PostForm onSubmit={handleSubmit}>
        <Styled.Heading>글 작성</Styled.Heading>
        <Styled.TitleInput placeholder="제목을 입력해주세요." value={title} onChange={e => setTitle(e.target.value)} />
        <Styled.ContentInput
          placeholder="내용을 작성해주세요."
          value={content}
          onChange={e => setContent(e.target.value)}
        />
        <Styled.PostButton>글 작성하기</Styled.PostButton>
      </Styled.PostForm>
    </Layout>
  );
};

export default CreatePostPage;
