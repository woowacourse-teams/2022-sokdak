import PaginationComponent from '@/components/Pagination';

import PandaIcon from '@/assets/images/panda_logo.svg';

import { invalidInputAnimation } from '@/style/GlobalStyle';
import styled from '@emotion/styled';

export const Container = styled.div`
  display: flex;
  width: 100%;
  flex-direction: column;
  padding: 1em 0;
`;

export const Avatar = styled.div`
  box-sizing: border-box;
  width: 150px;
  height: 150px;
  border: 1px solid ${props => props.theme.colors.gray_400};
  border-radius: 100%;

  display: flex;
  justify-content: center;
  align-items: center;
  align-self: center;
`;

export const Panda = styled(PandaIcon)`
  width: 140px;
  margin-left: -12px;
`;

export const FocusBorder = styled.span`
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 0;
  height: 1px;
  background-color: ${props => props.theme.colors.gray_400};
  transition: 0.4s;
`;

export const NicknameField = styled.form`
  width: 100%;
  display: flex;
  padding: 30px 0;
  position: relative;
  justify-content: center;
`;

export const NicknameInputField = styled.div`
  width: 100%;
  height: 30px;
  position: relative;
  display: flex;
  justify-content: center;
`;

export const Nickname = styled.input<{ length: number; isAnimationActive?: boolean }>`
  width: 200px;
  font-family: 'BMHANNAPro', 'Noto Sans KR';
  grid-column: 2;
  text-align: center;

  display: flex;
  justify-content: center;
  align-items: center;
  font-size: ${props => (props.length > 10 ? 30 - props.length + 'px' : '20px')};
  padding-bottom: 5px;

  :disabled {
    color: black;
    background-color: transparent;
  }

  ::placeholder {
    font-size: 15px;
  }

  :enabled ~ ${FocusBorder} {
    width: 200px;
    transition: 0.4s;
  }

  ${invalidInputAnimation}
`;

export const UpdateButton = styled.button`
  display: flex;
  justify-self: flex-end;
  align-items: center;
  background-color: transparent;
  color: ${props => props.theme.colors.pink_300};
  position: absolute;
  right: 0;
  top: 50%;
  transform: translate3d(0, -50%, 0);
`;

export const PostField = styled.div`
  display: flex;
  flex-direction: column;
`;

export const PostListHeader = styled.p`
  border-top: 1px solid ${props => props.theme.colors.gray_400};
  font-family: 'BMHANNAPro', 'Noto Sans KR';
  font-size: 20px;
  padding: 15px 0;
`;

export const PostList = styled.div`
  display: flex;
  flex-direction: column;
  height: 360px;
`;

export const Pagination = styled(PaginationComponent)`
  align-self: center;
  margin: 15px 0;
`;

export const EmptyPostList = styled.div`
  font-family: 'BMHANNAPro', 'Noto Sans KR';
  height: 360px;
  display: flex;
  justify-content: center;
  align-items: center;
  color: ${props => props.theme.colors.gray_200};
`;
