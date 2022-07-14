import { useContext, useState } from 'react';

import SnackbarContext from '@/context/Snackbar';

import * as Styled from './index.styles';

interface PostFormProps {
  heading: string;
  submitType: string;
  prevTitle?: string;
  prevContent?: string;
  handlePost: (post: Pick<Post, 'title' | 'content'>) => void;
}

const PostForm = ({ heading, submitType, prevTitle = '', prevContent = '', handlePost }: PostFormProps) => {
  const { isVisible, showSnackbar } = useContext(SnackbarContext);

  const [title, setTitle] = useState(prevTitle);
  const [content, setContent] = useState(prevContent);

  const [isValidTitle, setIsValidTitle] = useState(true);
  const [isValidContent, setIsValidContent] = useState(true);
  const [isAnimationActive, setIsAnimationActive] = useState(false);
  const [isContentAnimationActive, setIsContentAnimationActive] = useState(false);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    handlePost({ title, content });
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
      <Styled.SubmitButton>{submitType}</Styled.SubmitButton>
    </Styled.Container>
  );
};

export default PostForm;
