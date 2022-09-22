import React, { useState } from 'react';
import { useLocation } from 'react-router-dom';

import HashTagInput from './components/HashTagInput';

import useUploadImage from '@/hooks/queries/post/useUploadImage';
import useSnackbar from '@/hooks/useSnackbar';

import * as Styled from './index.styles';

import { BOARDS } from '@/constants/board';
import SNACKBAR_MESSAGE from '@/constants/snackbar';

const SubmitType = {
  POST: '글 작성하기',
  PUT: '글 수정하기',
} as const;

interface Image {
  file?: File;
  src?: string;
  path: string;
}

interface PostFormProps {
  heading: string;
  submitType: typeof SubmitType[keyof typeof SubmitType];
  prevTitle?: string;
  prevContent?: string;
  prevHashTags?: Omit<Hashtag, 'count'>[];
  prevImagePath?: string;
  handlePost: (
    post: Pick<Post, 'title' | 'content' | 'imageName'> & {
      hashtags: string[];
      anonymous?: boolean;
      boardId: string | number;
    },
  ) => void;
}

const PostForm = ({
  heading,
  submitType,
  prevTitle = '',
  prevContent = '',
  prevHashTags = [],
  prevImagePath = '',
  handlePost,
}: PostFormProps) => {
  const { isVisible, showSnackbar } = useSnackbar();

  const [title, setTitle] = useState(prevTitle);
  const [content, setContent] = useState(prevContent);
  const [hashtags, setHashtags] = useState(prevHashTags.map(hashtag => hashtag.name));
  const [anonymous, setAnonymous] = useState(true);
  const [image, setImage] = useState<Image>({
    path: prevImagePath,
  });

  const { boardId } = useLocation().state as Pick<Post, 'boardId'>;

  const { title: boardTitle } = BOARDS.find(board => board.id === Number(boardId))!;
  const [isValidTitle, setIsValidTitle] = useState(true);
  const [isValidContent, setIsValidContent] = useState(true);
  const [isAnimationActive, setIsAnimationActive] = useState(false);
  const [isContentAnimationActive, setIsContentAnimationActive] = useState(false);
  const { mutate: uploadImage, isLoading } = useUploadImage({
    onSuccess: ({ data }, variables) => {
      const image = variables.get('image') as File;

      setImage(image => ({ ...image, path: data.imageName }));
      preload(image);
    },
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    handlePost({ title, content, hashtags, anonymous, boardId, imageName: image.path });
  };

  const handleUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const formData = new FormData();
    const [file] = e.currentTarget.files!;

    if (!file) return;

    if (file.size > 1e7) {
      showSnackbar(SNACKBAR_MESSAGE.LARGE_IMAGE);
      return;
    }

    formData.append('image', file);
    uploadImage(formData);
  };

  const preload = (file: File) => {
    const reader = new FileReader();

    reader.onload = () =>
      setImage(image => ({
        ...image,
        file: file,
        src: typeof reader.result === 'string' ? reader.result : '',
      }));
    reader.readAsDataURL(file);
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
      <Styled.Board>{boardTitle}</Styled.Board>
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
      {image.path !== '' && !isLoading && (
        <Styled.ImagePreview>
          <Styled.Image src={image.file ? image.src : process.env.IMAGE_API_URL + image.path} alt={image.path} />
          <Styled.ImageName>{image.file ? image.file.name : image.path}</Styled.ImageName>
        </Styled.ImagePreview>
      )}
      {isLoading && (
        <Styled.ImageUploadLoading>
          이미지 업로드 중 . . <Styled.LoadingIcon>😵‍💫</Styled.LoadingIcon>
        </Styled.ImageUploadLoading>
      )}
      <HashTagInput hashtags={hashtags} setHashtags={setHashtags} />
      <Styled.SubmitButton>{submitType}</Styled.SubmitButton>
      <Styled.PostController>
        <Styled.CheckBox
          isChecked={anonymous}
          setIsChecked={setAnonymous}
          labelText="익명"
          visible={submitType === SubmitType.POST}
        />
        <Styled.ImageUploadButton htmlFor="file-upload">
          <Styled.CameraIcon />
        </Styled.ImageUploadButton>
        <Styled.ImageInput id="file-upload" type="file" accept="image/*" onChange={handleUpload} />
      </Styled.PostController>
    </Styled.Container>
  );
};

export default PostForm;
