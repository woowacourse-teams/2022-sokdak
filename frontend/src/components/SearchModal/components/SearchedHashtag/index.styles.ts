import HashTagComponent from '@/components/HashTag';

import { keyframes } from '@emotion/react';
import styled from '@emotion/styled';

const appear = keyframes`
  0% {
    opacity: 0;
  } 
  100% {
    opacity: 1;
  }
`;

export const Container = styled.div`
  max-width: 1140px;
  float: left;
  display: flex;
  gap: 5px;

  box-sizing: border-box;
  flex-wrap: wrap;
  height: fit-content;
  row-gap: 7px;
  justify-content: center;
  animation: ${appear} 0.2s;

  @media (min-width: 875px) {
    width: 100%;
    justify-content: start;
  }
`;

export const Hashtag = styled(HashTagComponent)`
  font-size: 12px;

  :hover {
    color: white;
    background-color: ${props => props.theme.colors.sub};

    span {
      color: white;
    }
  }
`;
