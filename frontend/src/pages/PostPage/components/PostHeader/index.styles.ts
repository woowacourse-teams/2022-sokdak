import { css, Theme } from '@emotion/react';
import styled from '@emotion/styled';

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
  font-family: 'BMHANNAPro', 'Noto Sans KR';
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

export const ReportButton = styled.button`
  background-color: inherit;
`;

export const PostInfo = styled.div`
  display: flex;
  justify-content: space-between;
  padding: 10px;
  margin: 0 -10px;
`;

export const Author = styled.span`
  font-size: 14px;
  font-family: 'BMHANNAPro', 'Noto Sans KR';
`;

export const Date = styled.span`
  min-width: 80px;
  text-align: right;
  font-size: 10px;
  color: ${props => props.theme.colors.gray_200};
`;

export const LikeButtonContainer = styled.div`
  width: 100%;
  display: flex;
  justify-content: flex-end;
`;
