import * as Styled from './index.styles';

interface SearchWebProps {
  onClickSearchIcon: React.MouseEventHandler<HTMLDivElement>;
}

const SearchWeb = ({ onClickSearchIcon }: SearchWebProps) => {
  return (
    <Styled.Search onClick={onClickSearchIcon}>
      <Styled.SearchIcon />
      <Styled.Placeholder>검색어를 입력해주세요.</Styled.Placeholder>
    </Styled.Search>
  );
};

export default SearchWeb;
