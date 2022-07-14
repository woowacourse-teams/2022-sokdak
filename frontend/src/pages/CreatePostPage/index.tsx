import { useContext, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import Layout from '@/components/@styled/Layout';
import Spinner from '@/components/Spinner';

import SnackbarContext from '@/context/Snackbar';

import useCreatePost from '@/hooks/queries/post/useCreatePost';

import * as Styled from './index.styles';

import PATH from '@/constants/path';

const CreatePostPage = () => {
  const [isValidTitle, setIsValidTitle] = useState(true);
  const [isValidContent, setIsValidContent] = useState(true);
  const [isAnimationActive, setIsAnimationActive] = useState(false);
  const [isContentAnimationActive, setIsContentAnimationActive] = useState(false);

  const inputRef = useRef<HTMLInputElement>(null);
  const navigate = useNavigate();
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const { isVisible, showSnackbar } = useContext(SnackbarContext);

  const { mutate: registerPost, isLoading } = useCreatePost({
    onSuccess: () => {
      navigate(PATH.HOME);
    },
  });

  if (isLoading) {
    return (
      <Layout>
        <Styled.SpinnerContainer>
          <Spinner />
        </Styled.SpinnerContainer>
      </Layout>
    );
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    registerPost({ title, content });
  };

  return (
    <Layout>
      <Styled.PostForm
        onSubmit={handleSubmit}
        onInvalid={(e: React.FormEvent<HTMLFormElement> & { target: HTMLInputElement }) => {
          if (isVisible) return;
          showSnackbar(e.target.placeholder);
        }}
      >
        <Styled.Heading>글 작성</Styled.Heading>
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
          onAnimationEnd={() => {
            setIsAnimationActive(false);
          }}
          isValid={isValidTitle}
          isAnimationActive={isAnimationActive}
          ref={inputRef}
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
          onAnimationEnd={() => {
            setIsContentAnimationActive(false);
          }}
          isValid={isValidContent}
          isAnimationActive={isContentAnimationActive}
          required
        />
        <Styled.PostButton>글 작성하기</Styled.PostButton>
      </Styled.PostForm>
    </Layout>
  );
};

export default CreatePostPage;
