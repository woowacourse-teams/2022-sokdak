import ArrowLeftSvg from '@/assets/images/arrow_left.svg';
import ArrowRightSvg from '@/assets/images/arrow_right.svg';

import styled from '@emotion/styled';

export const Container = styled.div`
  width: 100%;
  display: grid;
  align-items: center;
  grid-template-columns: 1fr 8fr 1fr;
  grid-template-areas:
    '. title .'
    'arrowLeft carousel arrowRight';
  @media (max-width: 875px) {
    display: none;
  }
`;

export const CarouselContainer = styled.div`
  width: 100%;
  max-width: 1400px;
  overflow-x: auto;
  box-sizing: border-box;
  display: -ms-inline-grid;
  align-items: center;
  gap: 1.5em;
  grid-area: carousel;
`;

export const Title = styled.p`
  width: calc(100% - 3em);
  max-width: 1400px;
  font-family: 'BMHANNAPro';
  font-size: 1.5rem;
  text-align: left;
  grid-area: title;
`;

export const PostContainer = styled.div`
  overflow: hidden;
`;

export const EmptyContainer = styled.div`
  width: 30px;
  grid-area: arrowLeft;
`;

export const ItemContainer = styled.div`
  width: 340px;
  padding: 1em;
  flex-shrink: 0;
`;

export const PostListContainer = styled.div<{ page: number }>`
  display: flex;
  width: 100%;
  align-items: center;
  gap: 1em;
  margin-left: ${props => `calc(-${props.page * 380}px - ${props.page * 0.5}em - 0.8em)`};
  transition: 1s;
`;

export const ArrowLeft = styled(ArrowLeftSvg)`
  cursor: pointer;
  grid-area: arrowLeft;
  justify-self: end;
  margin-right: 1rem;
`;

export const ArrowRight = styled(ArrowRightSvg)`
  cursor: pointer;
  grid-area: arrowRight;
  margin-left: 1rem;
`;
