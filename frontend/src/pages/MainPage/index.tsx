import BoardItem from './components/BoardItem';
import Layout from '@/components/@styled/Layout';
import Spinner from '@/components/Spinner';

import usePostByBoards from '@/hooks/queries/post/usePostsByBoard';

import * as Styled from './index.styles';

const MainPage = () => {
  const { isLoading, data } = usePostByBoards({});

  if (isLoading) {
    return (
      <Layout>
        <Spinner />
      </Layout>
    );
  }
  if (data)
    return (
      <Layout>
        <Styled.MainPageContainer>
          {data.boards.map(board => (
            <BoardItem key={board.id} {...board} />
          ))}
        </Styled.MainPageContainer>
      </Layout>
    );
  return <div />;
};

export default MainPage;
