import { useParams } from 'react-router-dom';

import NoResult from './components/NoResult';
import Layout from '@/components/@styled/Layout';
import PostList from '@/components/PostList';

import useSearchPostCount from '@/hooks/queries/post/useSearchPostCount';
import useSearchPosts from '@/hooks/queries/post/useSearchPosts';

import * as Styled from './index.styles';

const SearchedPostPage = () => {
  const { query } = useParams();
  if (!query) return null;

  const { data: postResult, fetchNextPage } = useSearchPosts({
    storeCode: [query.trim(), 5],
  });
  const { data: countResult } = useSearchPostCount({
    storeCode: [query.trim()],
  });

  return (
    <Layout>
      {query && postResult && countResult && (
        <Styled.Container>
          <Styled.Title>
            🔍 <Styled.Highlight>{query.replaceAll(' ', '+').replaceAll('+', ', ')}</Styled.Highlight> 관련{' '}
            {countResult.totalPostCount}개의 검색 결과
          </Styled.Title>
          {countResult.totalPostCount ? <PostList data={postResult} fetchNextPage={fetchNextPage} /> : <NoResult />}
        </Styled.Container>
      )}
    </Layout>
  );
};

export default SearchedPostPage;
