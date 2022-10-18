import { css } from '@emotion/react';
import styled from '@emotion/styled';

export const Container = styled.div`
  width: 100%;
  align-items: center;
  position: relative;

  @media (max-width: 875px) {
    display: none;
  }
`;

export const CarouselContainer = styled.div`
  width: 100%;
  overflow-x: hidden;
  box-sizing: border-box;
  align-items: center;
`;

export const Title = styled.p`
  font-family: 'BMHANNAPro';
  font-size: 1.5rem;
  margin-bottom: 0.5em;
  text-align: left;
`;

export const PostContainer = styled.div`
  overflow: hidden;
`;

export const ItemContainer = styled.div`
  width: 360px;
  padding: 0.5em;
  flex-shrink: 0;
`;

export const PostListContainer = styled.div<{ page: number }>`
  display: flex;
  width: 100%;
  align-items: center;
  gap: 1em;
  margin-left: ${props => `calc(-${props.page * 380}px - ${props.page * 0.5}em - 0.3em)`};
  transition: 1s;
`;

const buttonStyle = css`
  cursor: pointer;
  position: absolute;
  top: 50%;
  z-index: 10;
  width: 30px;
  height: 60px;
  font-size: 1rem;

  background-color: black;
  color: white;
  opacity: 0.6;

  display: flex;
  justify-content: center;
  align-items: center;
`;

export const ArrowLeft = styled.button`
  ${buttonStyle};
  left: 0;
  border-top-right-radius: 30px;
  border-bottom-right-radius: 30px;
`;

export const ArrowRight = styled.button`
  ${buttonStyle};
  right: 0;
  border-top-left-radius: 30px;
  border-bottom-left-radius: 30px;
`;
