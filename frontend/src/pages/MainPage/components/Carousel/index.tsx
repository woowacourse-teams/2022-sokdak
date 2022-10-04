import { useState } from 'react';

import PostListItem from '@/components/PostListItem';

import useThrottle from '@/hooks/useThrottle';

import * as Styled from './index.styles';

import useCarousel from './useCarousel';

const dummy: Omit<Post, 'id' | 'like' | 'hashtags' | 'authorized' | 'boardId' | 'nickname' | 'imageName'> = {
  blocked: false,
  commentCount: 3,
  content: '아무내용',
  createdAt: '2022-07-19T19:55:31.016376300',
  likeCount: 4,
  modified: false,
  title: '오늘 날씨 ㅁ맑네요',
};
const Carousel = () => {
  const [page, setPage] = useState(0);
  const { ref, isEnd, setIsEnd } = useCarousel();

  const handleClickLastPage = useThrottle(() => {
    if (isEnd) {
      setIsEnd(false);
    }
    setPage(prev => prev - 1);
  }, 300);

  const handleClickNextPage = useThrottle(() => {
    setPage(prev => prev + 1);
  }, 500);

  return (
    <Styled.Container>
      <Styled.Title>🔥 핫 게시판 🔥</Styled.Title>
      <Styled.CarouselContainer>
        {page !== 0 ? (
          <Styled.ArrowLeft width={'30px'} height={'30px'} onClick={handleClickLastPage} />
        ) : (
          <Styled.EmptyContainer />
        )}
        <Styled.PostContainer>
          <Styled.PostListContainer page={page}>
            {Array.from({ length: 10 }).map((item, idx) => (
              <Styled.ItemContainer key={idx} ref={idx === 9 ? ref : null}>
                <PostListItem {...dummy} handleClick={() => {}} testid={1} />
              </Styled.ItemContainer>
            ))}
          </Styled.PostListContainer>
        </Styled.PostContainer>

        {!isEnd && <Styled.ArrowRight width={'30px'} height={'30px'} onClick={handleClickNextPage} />}
      </Styled.CarouselContainer>
    </Styled.Container>
  );
};

export default Carousel;
