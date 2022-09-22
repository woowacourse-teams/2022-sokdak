import * as Styled from './index.styles';

interface ImagePreviewProps {
  image: Image;
  isLoading: boolean;
}

const ImagePreview = ({ image, isLoading }: ImagePreviewProps) => {
  return (
    <>
      {isLoading && (
        <Styled.ImageUploadLoading>
          이미지 업로드 중 . . <Styled.LoadingIcon>😵‍💫</Styled.LoadingIcon>
        </Styled.ImageUploadLoading>
      )}
      {image.path && !isLoading && (
        <Styled.ImagePreview>
          <Styled.Image src={image.file ? image.src : process.env.IMAGE_API_URL + image.path} alt={image.path} />
          <Styled.ImageName>{image.file ? image.file.name : image.path}</Styled.ImageName>
        </Styled.ImagePreview>
      )}
    </>
  );
};

export default ImagePreview;
