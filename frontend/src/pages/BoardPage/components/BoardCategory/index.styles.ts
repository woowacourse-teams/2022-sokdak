import { Link } from 'react-router-dom';

import Dropdown from '@/components/@shared/Dropdown';

import ChevronDown from '@/assets/images/chevron-down.svg';

import { css } from '@emotion/react';
import styled from '@emotion/styled';

export const BoardCategoryContainer = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
  margin: calc(1.5rem - 15px) 0 1.5rem 0;
  text-align: center;
`;

export const TitleContainer = styled.div`
  width: 12rem;
  display: flex;
  justify-content: center;
  align-items: center;
`;

export const Title = styled.p`
  font-family: 'BMHANNAPro', 'Noto Sans KR';
  white-space: nowrap;
  font-size: 1.2rem;
  color: black;
`;

export const BoardItem = styled(Link)`
  font-family: 'BMHANNAAir', 'Noto Sans KR';
  width: fit-content;
  font-size: 14px;
  color: ${props => props.theme.colors.gray_200};
  height: 100%;
  padding: 1em 0;
  white-space: nowrap;
`;

export const BoardList = styled.div`
  padding: 0.5em 1em;
  display: flex;
  width: 7rem;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: white;
  box-shadow: 0px 1px 7px rgb(0 0 0 / 13%);
  border-radius: 10px;
  & > ${BoardItem}:last-child {
    border: none;
  }
`;

export const MobileNavContainer = styled.nav`
  @media (min-width: 875px) {
    display: none;
  }
`;

export const DropdownIcon = styled(ChevronDown)`
  fill: ${props => props.theme.colors.gray_300};
  width: 30px;
  height: 30px;
  margin: 0 4px 0 10px;
`;

export const OptionList = styled(Dropdown.OptionList)`
  left: 46%;
`;

export const DesktopNavContainer = styled.nav`
  width: 100%;
  display: flex;
  overflow: auto;
  border-bottom: 1px solid ${props => props.theme.colors.gray_400};
  gap: 20px;
  margin-top: 2em;
  @media (max-width: 875px) {
    display: none;
  }
`;

export const DesktopCategoryStyle = css`
  font-family: 'BMHANNAAir', 'Noto Sans KR';
  font-size: 1.3rem;
  padding: 0.2em;
`;

export const DesktopCategory = styled.p`
  ${DesktopCategoryStyle}
  opacity: 0.5;
`;

export const ActiveDesktopCategory = styled.p`
  ${DesktopCategoryStyle}
  font-family: 'BMHANNAPro', 'Noto Sans KR';
  border-bottom: 3px solid black;
`;
