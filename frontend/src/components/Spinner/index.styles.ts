import { keyframes } from '@emotion/react';
import styled from '@emotion/styled';

export const Container = styled.div`
  width: 500px;
  height: 500px;
  display: flex;
  justify-content: center;
  align-items: center;
`;

const blink = keyframes`
  0%,
  10%,
  12%,
  20%,
  22%,
  40%,
  42%,
  60%,
  62%,
  70%,
  72%,
  90%,
  92%,
  98%,
  100% {
    height: 48px;
  }
  11%,
  21%,
  41%,
  61%,
  71%,
  91%,
  99% {
    height: 18px;
  }
`;
const eyeMove = keyframes`
  0%,
  10% {
    background-position: 0px 0px;
  }
  13%,
  40% {
    background-position: -15px 0px;
  }
  43%,
  70% {
    background-position: 15px 0px;
  }
  73%,
  90% {
    background-position: 0px 15px;
  }
  93%,
  100% {
    background-position: 0px 0px;
  }
`;

export const Spinner = styled.span`
  position: relative;
  width: 108px;
  display: flex;
  justify-content: space-between;

  &::after,
  &::before {
    content: '';
    display: inline-block;
    width: 48px;
    height: 48px;
    border: 2px solid black;
    background-color: #fff;
    background-image: radial-gradient(circle 14px, #0d161b 100%, transparent 0);
    background-repeat: no-repeat;
    border-radius: 50%;
    animation: ${eyeMove} 10s infinite, ${blink} 10s infinite;
  }
`;
