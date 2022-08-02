import { useParams } from 'react-router-dom';

import Layout from '@/components/@styled/Layout';
import PostList from '@/components/PostList';
import Spinner from '@/components/Spinner';

import usePostsByHashTag from '@/hooks/queries/post/usePostsByHashTag';

import * as Styled from './index.styles';

const HashTagPage = () => {
  const { name } = useParams();
  const { isLoading, isError, data, fetchNextPage } = usePostsByHashTag({ storeCode: [name!, 3] });

  if (isError) {
    return (
      <Styled.Error>
        <Styled.ErrorCode>404</Styled.ErrorCode>
        해당 해시태그 관련 게시물을 찾을 수 없습니다.
      </Styled.Error>
    );
  }

  return (
    <Layout>
      <Styled.Container>
        <Styled.HashTag># {name}</Styled.HashTag>
        <PostList data={data} fetchNextPage={fetchNextPage} />
        {isLoading && <Spinner />}
      </Styled.Container>
    </Layout>
  );
};

export default HashTagPage;
