import { Link } from 'react-router-dom';

import SearchIcon from '@/assets/images/search.svg';

import { css } from '@emotion/react';
import styled from '@emotion/styled';

export const Container = styled.header`
  width: 350px;
  height: 50px;
  display: flex;
  justify-content: space-between;
  margin: 15px auto;
  box-sizing: border-box;
`;

export const LeftSide = styled(Link)`
  display: flex;
  align-items: center;
  gap: 8px;
`;

export const RightSide = styled.div`
  display: flex;
  align-items: center;
  gap: 5px;
`;

export const Title = styled.p`
  font-size: 1.5rem;
  font-family: 'BMYEONSUNG';
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
  font-family: 'BMHANNAPro';
`;

export const Search = styled(SearchIcon)`
  cursor: pointer;
  margin: 10px;
  overflow: visible;
`;

export const LoginLink = styled(Link)`
  display: flex;
  justify-content: center;
  align-items: center;

  width: 50px;
  height: 20px;
  font-family: 'BMHANNAPro';
  font-size: 12px;
  color: ${props => props.theme.colors.gray_900};
  border: 1px solid black;
  border-radius: 8px;
`;

export const AvatarContainer = styled.div`
  display: flex;
  align-items: center;
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

export const ProfileButton = styled.button`
  ${optionStyle}
  color:${props => props.theme.colors.gray_150}
`;

export const LogoutButton = styled.button`
  ${optionStyle}
  color:${props => props.theme.colors.red_100}
`;
