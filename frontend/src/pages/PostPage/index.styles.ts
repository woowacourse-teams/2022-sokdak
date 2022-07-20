import { Link } from 'react-router-dom';

import { css, Theme } from '@emotion/react';
import styled from '@emotion/styled';

export const Container = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

export const HeadContainer = styled.div`
  width: 100%;
  min-height: 70px;
  display: flex;
  flex-direction: column;
  margin-bottom: 60px;
`;

export const TitleContainer = styled.div`
  width: 100%;
  word-wrap: break-word;
`;

export const Title = styled.p`
  font-size: 24px;
  font-family: 'BMHANNAPro';
  word-break: keep-all;
  line-height: 30px;
`;

export const PostController = styled.div`
  display: flex;
  justify-content: flex-end;
  margin: 0 -5px;
`;

const controllerButton = (props: { theme: Theme }) => css`
  font-size: 10px;
  background-color: transparent;
  color: ${props.theme.colors.gray_200};
  width: fit-content;
  padding: 5px;
`;

export const UpdateButton = styled.button`
  ${controllerButton}
`;

export const DeleteButton = styled.button`
  ${controllerButton}

  color: ${props => props.theme.colors.red_200};
`;

export const PostInfo = styled.div`
  display: flex;
  justify-content: space-between;
  padding: 10px;
  margin: 0 -10px;
`;

export const Author = styled.span`
  font-size: 14px;
  font-family: 'BMHANNAPro';
`;

export const Date = styled.span`
  min-width: 80px;
  text-align: right;
  font-size: 10px;
  color: ${props => props.theme.colors.gray_200};
`;

export const ContentContainer = styled.div`
  width: 100%;
  min-height: 420px;
  border-bottom: 1px solid ${props => props.theme.colors.sub};
  margin-bottom: 25px;
  padding-bottom: 20px;
`;

export const Content = styled.p`
  line-height: 25px;
`;

export const ListButtonContainer = styled.div`
  width: 100%;
  display: flex;
  justify-content: flex-end;
`;

export const ListButton = styled(Link)`
  font-family: 'BMHANNAAir';
  background-color: ${props => props.theme.colors.sub};
  color: white;
  border-radius: 15px;
  font-size: 17px;
  width: 100px;
  height: 55px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
`;

export const SpinnerContainer = styled.div`
  position: fixed;
  top: 20%;
  left: 50%;
  transform: translateY(-50%);
  transform: translateX(-50%);
`;

export const ErrorContainer = styled.div`
  width: 100%;
  height: 500px;
  font-family: 'BMHANNAPro';
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 3em;
`;

export const LikeButtonContainer = styled.div`
  width: 100%;
  display: flex;
  justify-content: flex-end;
`;
