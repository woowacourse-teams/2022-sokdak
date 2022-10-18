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

export const ItemContainer = styled.div`
  width: 360px;
  padding: 0.5em;
  flex-shrink: 0;
  scroll-snap-align: end;
`;

export const PostListContainer = styled.div`
  display: flex;
  width: 100%;
  align-items: center;
  gap: 1em;
  overflow: auto;
  scroll-behavior: smooth;
  scroll-snap-type: x proximity;
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
  left: -10px;
  border-top-right-radius: 30px;
  border-bottom-right-radius: 30px;
`;

export const ArrowRight = styled.button`
  ${buttonStyle};
  right: -10px;
  border-top-left-radius: 30px;
  border-bottom-left-radius: 30px;
`;
