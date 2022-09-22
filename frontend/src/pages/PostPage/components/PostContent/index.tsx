import { useNavigate } from 'react-router-dom';

import HashTag from '@/components/HashTag';

import * as Styled from './index.styles';

import PATH from '@/constants/path';

const PostContent = ({ content, hashtags, hasImage }: Pick<Post, 'hashtags' | 'content'> & { hasImage: boolean }) => {
  const navigate = useNavigate();

  const handleHashtagClick = (name: string) => {
    navigate(`${PATH.HASHTAG}/${name}`);
  };

  return (
    <Styled.ContentContainer hasImage={hasImage}>
      <Styled.Content>{content}</Styled.Content>
      <Styled.TagContainer>
        {hashtags?.map(({ name }) => (
          <HashTag key={name} name={name} handleTagClick={() => handleHashtagClick(name)} />
        ))}
      </Styled.TagContainer>
    </Styled.ContentContainer>
  );
};

export default PostContent;
