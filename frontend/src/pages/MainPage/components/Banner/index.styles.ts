import { keyframes } from '@emotion/react';
import styled from '@emotion/styled';

export const BannerContainer = styled.div`
  display: flex;
  justify-content: space-between;
  width: 100%;
  cursor: default;

  @media (max-width: 875px) {
    display: none;
  }
`;

export const LeftSide = styled.div`
  display: flex;
  align-items: center;
`;

export const RightSide = styled.div`
  display: flex;
  position: relative;
  justify-content: center;
  align-items: center;
  width: 45vw;
  height: 300px;
  border-radius: 30px;
  background-image: linear-gradient(120deg, #e0c6d2 0%, #f6ecdc 100%);
`;

export const BannerText = styled.p`
  width: 100%;
  font-weight: 700;
  font-family: 'Noto Sans KR';
  font-size: 3rem;
  line-height: 1.3em;
  word-break: keep-all;

  @media (max-width: 1200px) {
    font-size: 2.5rem;
  }
`;

const hvr_bob = keyframes`
    0% {
      -webkit-transform: translateY(-8px);
      transform: translateY(-8px);
    }
    50% {
      -webkit-transform: translateY(-4px);
      transform: translateY(-4px);
    }
    100% {
      -webkit-transform: translateY(-8px);
      transform: translateY(-8px);
    }
`;

const hvr_bob_float = keyframes`
    100% {
      -webkit-transform: translateY(-8px);
      transform: translateY(-8px);
    }
`;

export const HashtagContainer = styled.div<{ x: number; y: number }>`
  display: flex;
  position: absolute;
  top: 50%;
  left: 50%;
  justify-content: center;
  align-items: center;
  font-size: 1.5rem;
  width: 150px;
  height: 70px;
  transform: translate(-50%, -50%) ${props => `translate3d(${props.x}px,${props.y}px, 0px)`};
  transform-style: preserve-3d;

  @media (max-width: 1200px) {
    font-size: 1.2rem;
  }
`;

export const Hashtag = styled.a`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  border-radius: 20px;
  background-color: white;
  font-family: 'Noto Sans KR';
  font-weight: 700;
  font-size: 1.5rem;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
  box-shadow: 4px 4px 20px rgba(0, 0, 0, 0.25);
  cursor: pointer;

  :hover {
    -webkit-animation-name: ${hvr_bob_float}, ${hvr_bob};
    animation-name: ${hvr_bob_float}, ${hvr_bob};
    -webkit-animation-duration: 0.3s, 1.5s;
    animation-duration: 0.3s, 1.5s;
    -webkit-animation-delay: 0s, 0.3s;
    animation-delay: 0s, 0.3s;
    -webkit-animation-timing-function: ease-out, ease-in-out;
    animation-timing-function: ease-out, ease-in-out;
    -webkit-animation-iteration-count: 1, infinite;
    animation-iteration-count: 1, infinite;
    -webkit-animation-fill-mode: forwards;
    animation-fill-mode: forwards;
    -webkit-animation-direction: normal, alternate;
    animation-direction: normal, alternate;
  }
`;

export const HashtagText = styled.p`
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
`;
