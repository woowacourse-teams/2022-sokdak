import ArrowLeftSvg from '@/assets/images/arrow_left.svg';
import ArrowRightSvg from '@/assets/images/arrow_right.svg';

import styled from '@emotion/styled';

export const Container = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  @media (max-width: 875px) {
    display: none;
  }
`;

export const CarouselContainer = styled.div`
  width: calc(100% - 3em);
  max-width: 1400px;
  display: flex;
  align-items: center;
  gap: 1.5em;
`;

export const Title = styled.p`
  font-family: 'BMHANNAPro';
  font-size: 1.5rem;
  width: calc(100% - 3em);
  max-width: 1400px;
  text-align: left;
`;

export const PostContainer = styled.div`
  width: 90%;
  overflow: hidden;
  transition: 1s;
`;

export const EmptyContainer = styled.div`
  width: 30px;
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
  margin-left: ${props => `calc(-${props.page * 380}px - ${props.page * 0.5}em)`};
  transition: 1s;
`;

export const ArrowLeft = styled(ArrowLeftSvg)`
  cursor: pointer;
`;

export const ArrowRight = styled(ArrowRightSvg)`
  cursor: pointer;
`;
