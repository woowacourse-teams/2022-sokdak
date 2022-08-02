import { useNavigate, useParams } from 'react-router-dom';

import Layout from '@/components/@styled/Layout';
import FAB from '@/components/FAB';
import PostList from '@/components/PostList';
import Spinner from '@/components/Spinner';

import usePosts from '@/hooks/queries/post/usePosts';

import PATH from '@/constants/path';

const BoardPage = () => {
  const navigate = useNavigate();
  const { id: boardId } = useParams();
  const { isLoading, data, fetchNextPage } = usePosts({ storeCode: [boardId!, 3] });

  const handleClickFAB = () => {
    navigate(PATH.CREATE_POST);
  };

  return (
    <Layout>
      <PostList data={data} fetchNextPage={fetchNextPage} />
      {isLoading && <Spinner />}
      <FAB handleClick={handleClickFAB} />
    </Layout>
  );
};

export default BoardPage;
