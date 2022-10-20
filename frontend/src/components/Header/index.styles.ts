import { Link } from 'react-router-dom';

import PandaIcon from '@/assets/images/panda_logo.svg';
import SearchIcon from '@/assets/images/search.svg';

import { css } from '@emotion/react';
import styled from '@emotion/styled';

export const Container = styled.header`
  width: 80%;
  height: 50px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 15px auto;
  gap: 3em;
  box-sizing: border-box;

  @media (max-width: 875px) {
    width: 320px;
  }
`;

export const LeftSide = styled(Link)`
  display: flex;
  align-items: center;
  gap: 8px;
`;

export const RightSide = styled.div`
  display: flex;
  align-items: center;
`;

export const Title = styled.p`
  width: max-content;
  font-size: 1.5rem;
  font-family: 'BMHANNAPro';
`;

export const Avartar = styled.div`
  width: 40px;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 100%;
  background-color: inherit;
  border: 1px solid ${props => props.theme.colors.main};
  cursor: pointer;
  font-family: 'BMHANNAPro', 'Noto Sans KR';
`;

export const Search = styled(SearchIcon)`
  cursor: pointer;
  margin: 8px;
  overflow: visible;
`;

export const LoginLink = styled(Link)`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 50px;
  height: 20px;
  font-family: 'BMHANNAAir', 'Noto Sans KR';
  font-size: 12px;
  color: black;
  border: 1px solid ${props => props.theme.colors.gray_400};
  border-radius: 8px;
  @media (min-width: 875px) {
    width: 70px;
    height: 2.2rem;
    border-radius: 20px;
    font-size: 0.9rem;
  }
`;

export const AvatarContainer = styled.div`
  display: flex;
  align-items: center;
  gap: 5px;
`;

export const OptionsContainer = styled.div`
  display: flex;
  flex-direction: column;
  background-color: white;
  width: 5rem;
  border-radius: 4px;
  box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.25);
`;

export const optionStyle = css`
  background-color: transparent;
  padding: 1em;
  text-align: left;
  font-size: 0.7rem;
`;

export const ProfileLink = styled(Link)`
  ${optionStyle}
  color: ${props => props.theme.colors.gray_150};
`;

export const LogoutButton = styled.button`
  ${optionStyle}
  color: ${props => props.theme.colors.red_100};
`;

export const Panda = styled(PandaIcon)`
  width: 30px;
  height: 30px;
`;

export const DropdownContainer = styled.div`
  width: fit-content;

  @media (min-width: 875px) {
    margin-bottom: 7px;
  }
`;

export const SearchButton = styled.button`
  width: fit-content;
  background-color: transparent;
  height: fit-content;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0;
`;
