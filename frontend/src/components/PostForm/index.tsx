import React, { useState } from 'react';

import HashTag from '@/components/HashTag';

import useSnackbar from '@/hooks/useSnackbar';

import * as Styled from './index.styles';

import useHashTag from './useHashTag';

interface PostFormProps {
  heading: string;
  submitType: string;
  prevTitle?: string;
  prevContent?: string;
  prevHashTags?: Hashtag[];
  handlePost: (post: Pick<Post, 'title' | 'content'> & { hashtags: string[] }) => void;
}

const PostForm = ({
  heading,
  submitType,
  prevTitle = '',
  prevContent = '',
  prevHashTags = [],
  handlePost,
}: PostFormProps) => {
  const { isVisible, showSnackbar } = useSnackbar();

  const [title, setTitle] = useState(prevTitle);
  const [content, setContent] = useState(prevContent);
  const {
    hashtags,
    setHashtags,
    tagInputValue,
    handleTagInputChange,
    handleTagInputKeyDown,
    isTagInputFocus,
    handleTagInputFocus,
  } = useHashTag(prevHashTags.map(hashtag => hashtag.name));

  const [isValidTitle, setIsValidTitle] = useState(true);
  const [isValidContent, setIsValidContent] = useState(true);
  const [isAnimationActive, setIsAnimationActive] = useState(false);
  const [isContentAnimationActive, setIsContentAnimationActive] = useState(false);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    handlePost({ title, content, hashtags });
  };

  return (
    <Styled.Container
      onSubmit={handleSubmit}
      onInvalid={(e: React.FormEvent<HTMLFormElement> & { target: HTMLInputElement }) => {
        if (isVisible) return;
        showSnackbar(e.target.placeholder);
      }}
    >
      <Styled.Heading>{heading}</Styled.Heading>
      <Styled.TitleInput
        placeholder="제목을 입력해주세요."
        value={title}
        onChange={e => setTitle(e.target.value)}
        maxLength={50}
        onInvalid={e => {
          e.preventDefault();
          setIsValidTitle(false);
          setIsAnimationActive(true);
        }}
        onAnimationEnd={() => setIsAnimationActive(false)}
        isValid={isValidTitle}
        isAnimationActive={isAnimationActive}
        required
      />
      <Styled.ContentInput
        placeholder="내용을 작성해주세요."
        value={content}
        onChange={e => setContent(e.target.value)}
        maxLength={5000}
        onInvalid={e => {
          e.preventDefault();
          setIsValidContent(false);
          setIsContentAnimationActive(true);
        }}
        onAnimationEnd={() => setIsContentAnimationActive(false)}
        isValid={isValidContent}
        isAnimationActive={isContentAnimationActive}
        required
      />
      <Styled.TagContainer>
        {isTagInputFocus && (
          <Styled.TagTooltip>
            쉼표 혹은 엔터를 통해, <br />
            태그를 등록해보세요
          </Styled.TagTooltip>
        )}
        {hashtags.map(hashtagName => (
          <HashTag
            key={hashtagName}
            name={hashtagName}
            handleTagClick={() => setHashtags(tags => tags.filter(tag => tag !== hashtagName))}
          />
        ))}
        <Styled.TagInput
          placeholder="태그를 입력해주세요."
          value={tagInputValue}
          maxLength={15}
          onChange={handleTagInputChange}
          onKeyDown={handleTagInputKeyDown}
          onFocus={handleTagInputFocus}
          onBlur={handleTagInputFocus}
        />
      </Styled.TagContainer>
      <Styled.SubmitButton>{submitType}</Styled.SubmitButton>
    </Styled.Container>
  );
};

export default PostForm;
