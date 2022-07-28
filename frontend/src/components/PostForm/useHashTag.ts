import { useReducer, useState } from 'react';

import useSnackbar from '@/hooks/useSnackbar';

import SNACKBAR_MESSAGE from '@/constants/snackbar';

const useHashTag = (prevHashTags: string[]) => {
  const [hashtags, setHashtags] = useState<string[]>(prevHashTags);
  const [tagInputValue, setTagInputValue] = useState('');
  const [isTagInputFocus, handleTagInputFocus] = useReducer(state => !state, false);
  const { showSnackbar } = useSnackbar();

  const handleTagInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const endOfContent = e.target.value.includes(',');

    if (endOfContent) {
      addTag(e.target.value.replace(',', '').trim());
      return;
    }

    setTagInputValue(e.target.value);
  };

  const handleTagInputKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.nativeEvent.isComposing || e.code !== 'Enter') return;

    e.preventDefault();

    addTag(e.currentTarget.value.replace(',', '').trim());
  };

  const addTag = (tagContent: string) => {
    const isExistTag = hashtags.includes(tagContent);

    setTagInputValue('');

    if (!tagContent.replaceAll(' ', '')) {
      showSnackbar(SNACKBAR_MESSAGE.EMPTY_TAG);
      return;
    }

    if (isExistTag) {
      showSnackbar(SNACKBAR_MESSAGE.EXIST_TAG);
      return;
    }

    setHashtags(state => [...state, tagContent]);
  };

  return {
    hashtags,
    setHashtags,
    tagInputValue,
    handleTagInputChange,
    handleTagInputKeyDown,
    isTagInputFocus,
    handleTagInputFocus,
  };
};

export default useHashTag;
