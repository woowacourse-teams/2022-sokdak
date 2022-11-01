import Banner from './components/Banner';
import BoardItem from './components/BoardItem';
import Carousel from './components/Carousel';
import Footer from './components/Footer';

import usePostByBoards from '@/hooks/queries/post/usePostsByBoard';
import useResponsive from '@/hooks/useResponsive';

import * as Styled from './index.styles';

import { BOARDS } from '@/constants/board';

const MainPage = () => {
  const isDesktop = useResponsive(875);
  const { data: boards } = usePostByBoards({});

  if (!boards) {
    return <></>;
  }

  return (
    <>
      <Styled.Container>
        {isDesktop && <Banner />}
        {isDesktop && <Carousel />}
        <Styled.BoardItemContainer>
          {boards.map(board => (
            <BoardItem key={board.id} {...board} title={BOARDS[board.id - 1].title} boardId={board.id} />
          ))}
        </Styled.BoardItemContainer>
      </Styled.Container>
      <Footer />
    </>
  );
};

export default MainPage;
