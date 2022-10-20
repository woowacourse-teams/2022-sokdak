import { useParams } from 'react-router-dom';

import Layout from '@/components/@styled/Layout';
import PostList from '@/components/PostList';

import useSearchPostCount from '@/hooks/queries/post/useSearchPostCount';
import useSearchPosts from '@/hooks/queries/post/useSearchPosts';

import * as Styled from './index.styles';

const SearchedPostPage = () => {
  const { query } = useParams();
  if (!query) return null;

  const { data: postResult, fetchNextPage } = useSearchPosts({
    storeCode: [query.trim().replaceAll('+', '|'), 5],
  });
  const { data: countResult } = useSearchPostCount({
    storeCode: [query.trim().replaceAll('+', '|')],
  });

  return (
    <Layout>
      {query && postResult && countResult && (
        <Styled.Container>
          <Styled.Title>
            🔍 <Styled.Highlight>{query.replaceAll('+', ', ')}</Styled.Highlight> 관련 {countResult.totalPostCount}개의
            검색 결과
          </Styled.Title>
          <PostList data={postResult} fetchNextPage={fetchNextPage} />
        </Styled.Container>
      )}
    </Layout>
  );
};

export default SearchedPostPage;
