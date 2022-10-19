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
  font-size: 1.3rem;
  line-height: 1.6em;
  font-family: 'BMHANNAPro', 'Noto Sans KR';
  word-break: keep-all;

  @media (min-width: 875px) {
    font-size: 2rem;
  }
`;

export const PostController = styled.div`
  display: flex;
  justify-content: flex-end;
  margin: 0 -5px;
`;

const controllerButton = (props: { theme: Theme }) => css`
  font-size: 0.7rem;
  background-color: transparent;
  color: ${props.theme.colors.gray_200};
  width: fit-content;
  padding: 5px;

  @media (min-width: 875px) {
    font-size: 0.8rem;
  }
`;

export const UpdateButton = styled.button`
  ${controllerButton}
`;

export const DeleteButton = styled.button`
  ${controllerButton}

  color: ${props => props.theme.colors.red_200};
`;

export const ReportButton = styled.button`
  ${controllerButton};
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

  @media (min-width: 875px) {
    font-size: 1.1rem;
  }
`;

export const Date = styled.span`
  width: fit-content;
  text-align: right;
  font-size: 0.6rem;
  color: ${props => props.theme.colors.gray_200};

  @media (min-width: 875px) {
    font-size: 0.8rem;
  }
`;

export const ViewCount = styled.p`
  color: ${props => props.theme.colors.gray_600};
  width: max-content;
  margin-top: 10px;
  font-size: 0.8rem;
`;

export const CountContainer = styled.div`
  display: flex;
  justify-content: space-between;
`;
