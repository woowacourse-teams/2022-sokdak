import * as Styled from './index.styles';

interface SearchWebProps {
  onClickSearchIcon: React.MouseEventHandler<HTMLDivElement>;
}

const SearchWeb = ({ onClickSearchIcon }: SearchWebProps) => {
  return (
    <Styled.Search onClick={onClickSearchIcon}>
      <Styled.SearchImg />
      검색어를 입력해주세요.
    </Styled.Search>
  );
};

export default SearchWeb;
