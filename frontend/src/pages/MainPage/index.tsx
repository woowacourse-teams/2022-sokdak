import BoardItem from './components/BoardItem';
import Layout from '@/components/@styled/Layout';
import Spinner from '@/components/Spinner';

import usePostByBoards from '@/hooks/queries/post/usePostsByBoard';

import * as Styled from './index.styles';

import { BOARDS } from '@/constants/board';

const MainPage = () => {
  const { isLoading, data } = usePostByBoards({
    options: {
      staleTime: 1000 * 20,
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

  if (data)
    return (
      <Layout>
        <Styled.MainPageContainer>
          {data.boards.map(board => (
            <BoardItem key={board.id} {...board} title={BOARDS[board.id - 1].title} />
          ))}
        </Styled.MainPageContainer>
      </Layout>
    );

  return <div />;
};

export default MainPage;
