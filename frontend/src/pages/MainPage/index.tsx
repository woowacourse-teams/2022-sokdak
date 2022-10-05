import Banner from './components/Banner';
import BoardItem from './components/BoardItem';
import Carousel from './components/Carousel';
import Footer from './components/Footer';

import usePostByBoards from '@/hooks/queries/post/usePostsByBoard';

import * as Styled from './index.styles';

import { BOARDS } from '@/constants/board';

const MainPage = () => {
  const { data } = usePostByBoards({
    options: {
      staleTime: 1000 * 20,
    },
  });

  if (data)
    return (
      <>
        <Styled.MainPageContainer>
          <Banner />
          <Carousel />
          <Styled.BoardItemContainer>
            {data.boards.map(board => (
              <BoardItem key={board.id} {...board} title={BOARDS[board.id - 1].title} boardId={board.id} />
            ))}
          </Styled.BoardItemContainer>
        </Styled.MainPageContainer>
        <Footer />
      </>
    );

  return <div />;
};

export default MainPage;
