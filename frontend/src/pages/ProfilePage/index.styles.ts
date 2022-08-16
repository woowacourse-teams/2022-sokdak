import PaginationComponent from '@/components/Pagination';

import PandaIcon from '@/assets/images/panda_logo.svg';

import styled from '@emotion/styled';

export const Container = styled.div`
  display: flex;
  flex-direction: column;
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

export const NicknameField = styled.div`
  width: 100%;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  padding: 30px 0;
`;

export const Nickname = styled.p`
  font-family: 'BMHANNAPro';
  grid-column: 2;

  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 20px;
`;

export const UpdateButton = styled.button`
  display: flex;
  justify-self: flex-end;
  align-items: center;
  background-color: transparent;
  color: ${props => props.theme.colors.pink_300};
`;

export const PostField = styled.div`
  display: flex;
  flex-direction: column;
`;

export const PostListHeader = styled.p`
  border-top: 1px solid ${props => props.theme.colors.gray_400};
  font-family: 'BMHANNAPro';
  font-size: 20px;
  padding: 15px 0;
`;

export const PostList = styled.div`
  display: flex;
  flex-direction: column;
  height: 363px;
`;

export const Pagination = styled(PaginationComponent)`
  align-self: center;
  margin: 15px 0;
`;
