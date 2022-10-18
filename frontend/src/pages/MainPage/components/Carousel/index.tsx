import { useRef } from 'react';
import { useNavigate } from 'react-router-dom';

import PostListItem from '@/components/PostListItem';

import useHotPosts from '@/hooks/queries/post/useHotPosts';
import useThrottle from '@/hooks/useThrottle';

import * as Styled from './index.styles';

import PATH from '@/constants/path';

const Carousel = () => {
  const { data } = useHotPosts({ storeCode: [10], options: { suspense: true, staleTime: 1000 * 40 } });
  const navigate = useNavigate();
  const scrollContainer = useRef<HTMLDivElement>(null);
  const ref = useRef<HTMLDivElement>(null);

  const handleClickLastPage = useThrottle(() => {
    if (scrollContainer.current && ref.current) {
      scrollContainer.current.scrollLeft -= ref.current.clientWidth;
    }
  }, 300);

  const handleClickNextPage = useThrottle(() => {
    if (scrollContainer.current && ref.current) {
      scrollContainer.current.scrollLeft += ref.current.clientWidth;
    }
  }, 500);

  const handleClickPostItem = (id: number) => {
    navigate(`${PATH.POST}/${id}`);
  };

  return (
    <>
      {data?.posts.length !== 0 && (
        <Styled.Container>
          <Styled.Title>🔥 핫 게시판 🔥</Styled.Title>
          <Styled.ArrowLeft onClick={handleClickLastPage}>{'<'}</Styled.ArrowLeft>
          <Styled.ArrowRight onClick={handleClickNextPage}>{'>'}</Styled.ArrowRight>

          <Styled.CarouselContainer>
            <Styled.PostListContainer ref={scrollContainer}>
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
          </Styled.CarouselContainer>
        </Styled.Container>
      )}
    </>
  );
};

export default Carousel;
