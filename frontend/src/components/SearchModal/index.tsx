import ReactDOM from 'react-dom';

import * as Styled from './index.styles';

interface SearchModalProps {
  handleSearchModal: React.DispatchWithoutAction;
}

const SearchModal = ({ handleSearchModal }: SearchModalProps) => {
  return (
    <>
      {ReactDOM.createPortal(
        <Styled.Container>
          <Styled.Header>
            <Styled.InputContainer>
              <Styled.SearchIcon />
              <Styled.Input placeholder="검색어를 입력하세요." autoFocus />
            </Styled.InputContainer>
            <Styled.CloseButton onClick={handleSearchModal}>취소</Styled.CloseButton>
          </Styled.Header>

          <Styled.Content>
            <Styled.HashTagContainer>
              <Styled.HashTag name="일상" />
            </Styled.HashTagContainer>
          </Styled.Content>
        </Styled.Container>,
        document.getElementById('search-modal')!,
      )}
    </>
  );
};

export default SearchModal;
