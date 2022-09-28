import { StateAndAction } from 'sokdak-util-types';

import HashTag from '@/components/HashTag';

import * as Styled from './index.styles';

import useHashTagInput from './useHashTagInput';

const HashTagInput = ({ hashtags, setHashtags }: StateAndAction<string[], 'hashtags'>) => {
  const { tagInputValue, handleTagInputChange, handleTagInputKeyDown, isTagInputFocus, handleTagInputFocus } =
    useHashTagInput({ hashtags, setHashtags });

  return (
    <Styled.Container>
      {isTagInputFocus && (
        <Styled.Tooltip>
          쉼표 혹은 엔터를 통해, <br />
          태그를 등록해보세요
        </Styled.Tooltip>
      )}
      {hashtags.map(hashtagName => (
        <HashTag
          key={hashtagName}
          name={hashtagName}
          handleTagClick={() => setHashtags(tags => tags.filter(tag => tag !== hashtagName))}
        />
      ))}
      <Styled.Input
        placeholder="태그를 입력해주세요."
        value={tagInputValue}
        maxLength={15}
        onChange={handleTagInputChange}
        onKeyDown={handleTagInputKeyDown}
        onFocus={handleTagInputFocus}
        onBlur={handleTagInputFocus}
      />
    </Styled.Container>
  );
};

export default HashTagInput;
