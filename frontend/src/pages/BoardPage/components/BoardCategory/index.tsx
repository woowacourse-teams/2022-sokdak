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
      <div>
        <Dropdown>
          <Dropdown.Trigger>
            <Styled.TitleContainer>
              <Styled.Title>{BOARDS[Number(id) - 1].title}</Styled.Title>
              <Styled.DropdownIcon />
            </Styled.TitleContainer>
          </Dropdown.Trigger>
          <Dropdown.OptionList>
            <Styled.BoardList>
              {boardList.map(({ id }) => (
                <Styled.BoardItem key={id} to={`/board/${id}`}>
                  {BOARDS[id - 1].title}
                </Styled.BoardItem>
              ))}
            </Styled.BoardList>
          </Dropdown.OptionList>
        </Dropdown>
      </div>
    </Styled.BoardCategoryContainer>
  );
};

export default BoardCategory;
