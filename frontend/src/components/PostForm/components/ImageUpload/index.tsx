import { UseMutateAsyncFunction } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import useSnackbar from '@/hooks/useSnackbar';

import * as Styled from './index.styles';

import SNACKBAR_MESSAGE from '@/constants/snackbar';

interface ImageUploadProps {
  setImage: React.Dispatch<React.SetStateAction<Image>>;
  uploadImage: UseMutateAsyncFunction<AxiosResponse, AxiosError, FormData>;
}

const ImageUpload = ({ setImage, uploadImage }: ImageUploadProps) => {
  const { showSnackbar } = useSnackbar();

  const handleUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const formData = new FormData();
    const [file] = e.currentTarget.files!;

    if (!file) return;

    if (file.size > 1e7) {
      showSnackbar(SNACKBAR_MESSAGE.LARGE_IMAGE);
      return;
    }

    formData.append('image', file);

    const response = await uploadImage(formData);

    if (response.status === 200) {
      setImage(image => ({ ...image, path: response.data.imageName }));
      preload(file);
    }
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
    <>
      <Styled.ImageUploadButton htmlFor="file-upload">
        <Styled.CameraIcon />
      </Styled.ImageUploadButton>
      <Styled.ImageInput id="file-upload" type="file" accept="image/*" onChange={handleUpload} />
    </>
  );
};

export default ImageUpload;
