import { useState } from 'react';
import ReactDOM from 'react-dom';

import SearchedHashtag from './components/SearchedHashtag';
import SearchedPost from './components/SearchedPost';

import useSearchHashtags from '@/hooks/queries/hashtag/useSearchHashtags';
import useSearchPostCount from '@/hooks/queries/post/useSearchPostCount';
import useSearchPosts from '@/hooks/queries/post/useSearchPosts';
import useQueryDebounce from '@/hooks/queries/useQueryDebounce';

import * as Styled from './index.styles';

import useModalHistory from './useModalHistory';

interface SearchModalProps {
  handleSearchModal: React.DispatchWithoutAction;
}

const SearchModal = ({ handleSearchModal: closeModal }: SearchModalProps) => {
  const limit = 10;
  const [include, setInclude] = useState('');
  const { debounceValue: debouncedInclude } = useQueryDebounce(include);
  useModalHistory({ closeModal });

  const { data: hashtagResult } = useSearchHashtags({
    storeCode: [limit, debouncedInclude],
  });
  const { data: postResult } = useSearchPosts({
    storeCode: [debouncedInclude.trim(), 3],
    options: {
      enabled: !!debouncedInclude,
    },
  });
  const { data: postResultCount } = useSearchPostCount({
    storeCode: [debouncedInclude.trim()],
    options: {
      enabled: !!debouncedInclude,
    },
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
            {hashtagResult && <SearchedHashtag hashtags={hashtagResult.hashtags} closeModal={closeModal} />}
            {postResult && postResultCount && (
              <SearchedPost
                posts={postResult.pages}
                totalPostCount={postResultCount.totalPostCount}
                keyword={debouncedInclude}
                closeModal={closeModal}
              />
            )}
          </Styled.Content>
        </Styled.Container>,
        document.getElementById('search-modal')!,
      )}
    </>
  );
};

export default SearchModal;
