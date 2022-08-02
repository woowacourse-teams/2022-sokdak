import { useParams } from 'react-router-dom';

import Layout from '@/components/@styled/Layout';
import PostList from '@/components/PostList';
import Spinner from '@/components/Spinner';

import usePostsByHashTag from '@/hooks/queries/post/usePostsByHashTag';

const HashTagPage = () => {
  const { name } = useParams();
  const { isLoading, data, fetchNextPage } = usePostsByHashTag({ storeCode: [name!, 3] });

  return (
    <Layout>
      <PostList data={data} fetchNextPage={fetchNextPage} />
      {isLoading && <Spinner />}
    </Layout>
  );
};

export default HashTagPage;
