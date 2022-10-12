import HashTagComponent from '@/components/HashTag';

import Search from '@/assets/images/search.svg';

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
  width: 100%;
  height: 100vh;

  position: fixed;
  z-index: 10;
  background-color: white;

  top: 0;
  left: 50%;
  transform: translate(-50%, 0);
`;

export const Header = styled.div`
  height: 90px;
  width: 100%;
  margin-top: 25px;
  padding: 0 0 0 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  box-sizing: border-box;
  animation: ${appear} 0.2s;
`;

export const InputContainer = styled.div`
  max-width: 1140px;
  width: 100%;
  height: 40px;
  border: 0.5px solid ${props => props.theme.colors.gray_200};
  border-radius: 5px;
  display: flex;
  align-items: center;
  box-sizing: border-box;
`;

export const SearchIcon = styled(Search)`
  margin: 0 15px;
`;

export const Input = styled.input`
  height: 90%;
  width: 80%;
  padding: 0;

  ::placeholder {
    font-size: 0.8rem;
  }
`;

export const CloseButton = styled.button`
  width: 55px;
  height: 40px;
  background-color: transparent;
  color: ${props => props.theme.colors.gray_200};
  font-size: 12px;
`;

export const Content = styled.div`
  width: 100%;
  height: calc(100% - 110px);
  float: left;
  display: flex;
  padding: 0 20px;
  box-sizing: border-box;
  justify-content: center;
`;

export const HashTagContainer = styled.div`
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
`;

export const HashTag = styled(HashTagComponent)`
  font-size: 12px;

  :hover {
    color: white;
    background-color: ${props => props.theme.colors.sub};

    span {
      color: white;
    }
  }
`;
