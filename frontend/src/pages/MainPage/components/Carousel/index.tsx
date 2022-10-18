import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import PostListItem from '@/components/PostListItem';

import useHotPosts from '@/hooks/queries/post/useHotPosts';
import useThrottle from '@/hooks/useThrottle';

import * as Styled from './index.styles';

import PATH from '@/constants/path';

import useCarousel from './useCarousel';

const Carousel = () => {
  const [page, setPage] = useState(0);
  const { data } = useHotPosts({ storeCode: [10], options: { suspense: true, staleTime: 1000 * 40 } });
  const navigate = useNavigate();

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

  const handleClickPostItem = (id: number) => {
    navigate(`${PATH.POST}/${id}`);
  };

  return (
    <>
      {data?.posts.length !== 0 && (
        <Styled.Container>
          <Styled.Title>🔥 핫 게시판 🔥</Styled.Title>
          {page !== 0 && <Styled.ArrowLeft onClick={handleClickLastPage}>{'<'}</Styled.ArrowLeft>}
          {!isEnd && <Styled.ArrowRight onClick={handleClickNextPage}>{'>'}</Styled.ArrowRight>}

          <Styled.CarouselContainer>
            <Styled.PostContainer>
              <Styled.PostListContainer page={page}>
                {data?.posts.map((item, idx) => (
                  <Styled.ItemContainer key={item.id} ref={idx === data.posts.length - 1 ? ref : null}>
                    <PostListItem
                      {...item}
                      handleClick={() => {
                        handleClickPostItem(item.id);
                      }}
                      testid={item.id}
                    />
                  </Styled.ItemContainer>
                ))}
              </Styled.PostListContainer>
            </Styled.PostContainer>
          </Styled.CarouselContainer>
        </Styled.Container>
      )}
    </>
  );
};

export default Carousel;
