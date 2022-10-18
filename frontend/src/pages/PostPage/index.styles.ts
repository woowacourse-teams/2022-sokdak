import { Link } from 'react-router-dom';

import { css } from '@emotion/react';
import styled from '@emotion/styled';

export const containerStyle = css`
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;

  @media (min-width: 875px) {
    padding-top: 3rem;
    max-width: 790px;
  }
`;

export const Image = styled.img`
  min-height: 300px;
  max-width: 100%;
  border-radius: 5px;
  margin-bottom: 50px;
  object-fit: scale-down;
`;

export const Block = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: calc(100vh - 166px);
  overflow: hidden;
  gap: 2em;
`;

export const ErrorMessage = styled.p`
  font-family: 'BMHANNAAir', 'Noto Sans KR';
  text-align: center;
  line-height: 1.5rem;
  font-size: 1rem;

  @media (min-width: 875px) {
    font-size: 1.5rem;
    line-height: 3rem;
  }
`;

export const HandlingButtonContainer = styled.div`
  display: flex;
  gap: 10px;
`;

export const MainButton = styled.button`
  font-family: 'BMHANNAAir', 'Noto Sans KR';
  background-color: transparent;
  color: ${props => props.theme.colors.sub};
  padding: 0.8rem;
  border-radius: 3px;
  font-size: 1rem;
  letter-spacing: 1px;
`;

export const BackButton = styled.button`
  font-family: 'BMHANNAAir', 'Noto Sans KR';
  background-color: ${props => props.theme.colors.sub};
  color: white;
  padding: 0.8rem;
  border-radius: 3px;
  font-size: 1rem;
  letter-spacing: 1px;
`;

export const ListButtonContainer = styled.div`
  width: 100%;
  display: flex;
  justify-content: flex-end;
`;

export const ListButton = styled(Link)`
  font-family: 'BMHANNAAir', 'Noto Sans KR';
  background-color: ${props => props.theme.colors.sub};
  color: white;
  border-radius: 8px;
  font-size: 13px;
  padding: 4% 6%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
`;

export const SpinnerContainer = styled.div`
  width: 100%;
  height: calc(100vh - 100px);
  display: flex;
  justify-content: center;
  align-items: center;
`;

export const NotFound = styled.div`
  width: 100%;
  height: calc(100vh - 100px);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 3em;
  margin: auto;
`;

export const Container = styled.div`
  width: 90%;
  display: flex;
  justify-content: space-between;
  position: relative;
  padding: 1rem 0;

  @media (min-width: 875px) {
    width: 100%;
    padding: 4rem 0;
  }
`;

export const PostContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;

  @media (min-width: 875px) {
    width: 55%;
  }
`;
