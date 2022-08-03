import { useNavigate } from 'react-router-dom';

import Layout from '@/components/@styled/Layout';
import PostForm from '@/components/PostForm';
import Spinner from '@/components/Spinner';

import useCreatePost from '@/hooks/queries/post/useCreatePost';

import * as Styled from './index.styles';

const CreatePostPage = () => {
  const navigate = useNavigate();
  const { mutate: registerPost, isLoading } = useCreatePost({
    onSuccess: () => {
      navigate(-1);
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

  return (
    <Layout>
      <PostForm heading="글 작성" submitType="글 작성하기" handlePost={registerPost} />
    </Layout>
  );
};

export default CreatePostPage;
