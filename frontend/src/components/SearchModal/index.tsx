import { useState } from 'react';
import ReactDOM from 'react-dom';

import useHashtags from '@/hooks/queries/hashtag/useHashtags';
import useQueryDebounce from '@/hooks/queries/hashtag/useQueryDebounce';

import * as Styled from './index.styles';

import useModalHistory from './useModalClose';

interface SearchModalProps {
  handleSearchModal: React.DispatchWithoutAction;
}

const SearchModal = ({ handleSearchModal: closeModal }: SearchModalProps) => {
  const [limit] = useState(10);
  const [include, setInclude] = useState('');
  const { debounceValue: debouncedInclude } = useQueryDebounce(include);
  useModalHistory({ closeModal });

  const { data } = useHashtags({
    storeCode: [limit, debouncedInclude],
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
            <Styled.CloseButton onClick={closeModal}>취소</Styled.CloseButton>
          </Styled.Header>

          <Styled.Content>
            {data && (
              <Styled.HashTagContainer>
                {data.data.hashtags.map(({ name, count }) => (
                  <Styled.HashTag key={name} name={name} count={count} />
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
