import { NavLink } from 'react-router-dom';

import Dropdown from '@/components/@shared/Dropdown';

import * as Styled from './index.styles';

import { BOARDS } from '@/constants/board';

interface BoardCategoryProps {
  id: string;
  boards: Board[];
}

const BoardCategory = ({ id, boards }: BoardCategoryProps) => {
  const boardList = boards.filter(board => board.id !== Number(id));

  return (
    <Styled.BoardCategoryContainer>
      <Styled.DesktopNavContainer>
        {boards.map(({ id }) => (
          <NavLink key={id} to={`/board/${id}`}>
            {({ isActive }) =>
              isActive ? (
                <Styled.ActiveDesktopCategory>{BOARDS[id - 1].title}</Styled.ActiveDesktopCategory>
              ) : (
                <Styled.DesktopCategory>{BOARDS[id - 1].title}</Styled.DesktopCategory>
              )
            }
          </NavLink>
        ))}
      </Styled.DesktopNavContainer>
      <Styled.MobileNavContainer>
        <Dropdown>
          <Dropdown.Trigger>
            <Styled.TitleContainer>
              <Styled.Title>{BOARDS[Number(id) - 1].title}</Styled.Title>
              <Styled.DropdownIcon />
            </Styled.TitleContainer>
          </Dropdown.Trigger>
          <Styled.OptionList>
            <Styled.BoardList>
              {boardList.map(({ id }) => (
                <Styled.BoardItem key={id} to={`/board/${id}`}>
                  {BOARDS[id - 1].title}
                </Styled.BoardItem>
              ))}
            </Styled.BoardList>
          </Styled.OptionList>
        </Dropdown>
      </Styled.MobileNavContainer>
    </Styled.BoardCategoryContainer>
  );
};

export default BoardCategory;
