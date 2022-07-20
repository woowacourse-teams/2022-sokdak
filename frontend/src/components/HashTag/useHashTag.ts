import { useContext, useState } from 'react';

import SnackbarContext from '@/context/Snackbar';

import SNACKBAR_MESSAGE from '@/constants/snackbar';

const useHashTag = (prevHashTags: string[]) => {
  const [tags, setTags] = useState<string[]>(prevHashTags);
  const [tagInputValue, setTagInputValue] = useState('');
  const { showSnackbar } = useContext(SnackbarContext);

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
    const isExistTag = tags.includes(tagContent);

    setTagInputValue('');

    if (!tagContent.replaceAll(' ', '')) {
      showSnackbar(SNACKBAR_MESSAGE.EMPTY_TAG);
      return;
    }

    if (isExistTag) {
      showSnackbar(SNACKBAR_MESSAGE.EXIST_TAG);
      return;
    }

    setTags(state => [...state, tagContent]);
  };

  return {
    tags,
    tagInputValue,
    handleTagInputChange,
    handleTagInputKeyDown,
  };
};

export default useHashTag;
