import { useEffect } from 'react';
import { useParams } from 'react-router-dom';

import BoardCategory from './components/BoardCategory';
import Notice from './components/Notice';
import Layout from '@/components/@styled/Layout';
import PostList from '@/components/PostList';

import useBoards from '@/hooks/queries/board/useBoards';
import usePosts from '@/hooks/queries/post/usePosts';

import * as Styled from './index.styles';

const PRE_CREW_BOARD = 5;

const BoardPage = () => {
  const { id: boardId } = useParams();
  const { data: boards } = useBoards({
    options: {
      staleTime: Infinity,
    },
  });

  const { data, fetchNextPage, refetch } = usePosts({ storeCode: [boardId!, 3] });

  useEffect(() => {
    refetch();
  }, [boardId]);

  if (!boards?.some(({ id }) => id === Number(boardId))) {
    return <Layout></Layout>;
  }

  return (
    <Layout>
      <Styled.BoardContainer>
        <BoardCategory id={boardId!} boards={boards!} />
        {Number(boardId) === PRE_CREW_BOARD && <Notice />}
        <PostList data={data} fetchNextPage={fetchNextPage} />
      </Styled.BoardContainer>
    </Layout>
  );
};

export default BoardPage;
