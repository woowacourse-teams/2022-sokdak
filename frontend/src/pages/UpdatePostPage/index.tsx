import { useLocation, useNavigate } from 'react-router-dom';

import Layout from '@/components/@styled/Layout';
import PostForm from '@/components/PostForm';
import Spinner from '@/components/Spinner';

import useUpdatePost from '@/hooks/queries/post/useUpdatePost';

import * as Styled from './index.styles';

import NotFoundPage from '../NotFoundPage';

const UpdatePostPage = () => {
  const location = useLocation();
  const navigate = useNavigate();

  if (!location.state) {
    return <NotFoundPage />;
  }

  const { id, title, content, hashtags, imageName } = location.state as Omit<Post, 'createdAt'>;

  const { mutate: updatePost, isLoading } = useUpdatePost({
    id: String(id),
    options: {
      onSuccess: () => {
        navigate(-1);
      },
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
      <PostForm
        heading="글 수정"
        submitType="글 수정하기"
        prevTitle={title}
        prevContent={content}
        prevHashTags={hashtags}
        prevImagePath={imageName}
        handlePost={updatePost}
      />
    </Layout>
  );
};

export default UpdatePostPage;
