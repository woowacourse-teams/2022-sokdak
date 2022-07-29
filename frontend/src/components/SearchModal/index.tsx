import { useState } from 'react';
import ReactDOM from 'react-dom';

import useHashtags from '@/hooks/queries/hashtag/useHashtags';

import * as Styled from './index.styles';

interface SearchModalProps {
  handleSearchModal: React.DispatchWithoutAction;
}

const SearchModal = ({ handleSearchModal }: SearchModalProps) => {
  const [limit] = useState(10);
  const [include, setInclude] = useState('');

  const { data } = useHashtags({
    storeCode: [limit, include],
  });

  return (
    <>
      {ReactDOM.createPortal(
        <Styled.Container>
          <Styled.Header>
            <Styled.InputContainer>
              <Styled.SearchIcon />
              <Styled.Input
                placeholder="검색어를 입력하세요."
                value={include}
                onChange={e => setInclude(e.target.value)}
                autoFocus
              />
            </Styled.InputContainer>
            <Styled.CloseButton onClick={handleSearchModal}>취소</Styled.CloseButton>
          </Styled.Header>

          <Styled.Content>
            {data && (
              <Styled.HashTagContainer>
                {data.data.hashtags.map(({ name }) => (
                  <Styled.HashTag key={name} name={name} />
                ))}
              </Styled.HashTagContainer>
            )}
          </Styled.Content>
        </Styled.Container>,
        document.getElementById('search-modal')!,
      )}
    </>
  );
};

export default SearchModal;
