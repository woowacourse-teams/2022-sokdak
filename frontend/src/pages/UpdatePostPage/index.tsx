import { useLocation } from 'react-router-dom';

import Layout from '@/components/@styled/Layout';
import PostForm from '@/components/PostForm';

const UpdatePostPage = () => {
  const location = useLocation();
  const { title, content } = location.state as Pick<Post, 'content' | 'title'>;

  return (
    <Layout>
      <PostForm
        heading="글 수정"
        submitType="글 수정하기"
        prevTitle={title}
        prevContent={content}
        handlePost={() => {}}
      />
    </Layout>
  );
};

export default UpdatePostPage;
