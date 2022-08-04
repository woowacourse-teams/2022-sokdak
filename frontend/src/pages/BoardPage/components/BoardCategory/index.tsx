import Dropdown from '@/components/@shared/Dropdown';

import * as Styled from './index.styles';

interface BoardCategory {
  id: string;
  boards: Board[];
}

const BoardCategory = ({ id, boards }: BoardCategory) => {
  const currentBoard = boards.find(board => board.id === Number(id));
  const boardList = boards.filter(board => board.id !== Number(id));
  return (
    <Styled.BoardCategoryContainer>
      <div>
        <Dropdown>
          <Dropdown.Trigger>
            <Styled.TitleContainer>
              <Styled.Title>{currentBoard?.title}</Styled.Title>
              <Styled.DropdownIcon />
            </Styled.TitleContainer>
          </Dropdown.Trigger>
          <Styled.OptionList>
            <Styled.BoardList>
              {boardList.map(({ id, title }) => (
                <Styled.BoardItem key={id} to={`/board/${id}`}>
                  {title}
                </Styled.BoardItem>
              ))}
            </Styled.BoardList>
          </Styled.OptionList>
        </Dropdown>
      </div>
    </Styled.BoardCategoryContainer>
  );
};

export default BoardCategory;
