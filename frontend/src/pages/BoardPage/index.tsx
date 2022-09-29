import { useEffect } from 'react';
import { useParams } from 'react-router-dom';

import BoardCategory from './components/BoardCategory';
import Layout from '@/components/@styled/Layout';
import PostList from '@/components/PostList';
import Spinner from '@/components/Spinner';

import useBoards from '@/hooks/queries/board/useBoards';
import usePosts from '@/hooks/queries/post/usePosts';

import * as Styled from './index.styles';

const BoardPage = () => {
  const { id: boardId } = useParams();
  const { isLoading: boardIsLoading, data: boards } = useBoards({
    options: {
      staleTime: Infinity,
    },
  });
  const { isLoading, data, fetchNextPage, refetch } = usePosts({ storeCode: [boardId!, 3] });

  useEffect(() => {
    refetch();
  }, [boardId]);

  if (boardIsLoading || isLoading) {
    return (
      <Layout>
        <Styled.SpinnerContainer>
          <Spinner />
        </Styled.SpinnerContainer>
      </Layout>
    );
  }

  if (!boards?.some(({ id }) => id === Number(boardId))) {
    return <Layout></Layout>;
  }

  return (
    <Layout>
      <Styled.BoardContainer>
        <BoardCategory id={boardId!} boards={boards!} />
        <PostList data={data} fetchNextPage={fetchNextPage} />
      </Styled.BoardContainer>
    </Layout>
  );
};

export default BoardPage;
