import { Link } from 'react-router-dom';

import Dropdown from '@/components/@shared/Dropdown';

import ChevronDown from '@/assets/images/chevron-down.svg';

import styled from '@emotion/styled';

export const BoardCategoryContainer = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
  margin-bottom: 3rem;
  text-align: center;
`;

export const TitleContainer = styled.div`
  width: 12rem;
  display: flex;
  justify-content: center;
  align-items: center;
`;

export const Title = styled.p`
  font-family: 'BMHANNAPro';
  white-space: nowrap;
  font-size: 1.2rem;
`;

export const BoardItem = styled(Link)`
  width: 100%;
  font-size: 1rem;
  border-bottom: 0.5px solid ${props => props.theme.colors.gray_150};
  text-align: left;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  padding: 1em 0;
`;

export const BoardList = styled.div`
  padding: 0 1em;
  display: flex;
  width: 10rem;
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

export const DropdownIcon = styled(ChevronDown)`
  fill: ${props => props.theme.colors.gray_300};
  width: 30px;
  height: 30px;
  margin: 0 4px 0 10px;
`;

export const OptionList = styled(Dropdown.OptionList)`
  left: 46%;
`;
