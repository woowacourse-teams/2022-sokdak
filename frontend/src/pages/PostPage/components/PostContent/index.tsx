import { useNavigate } from 'react-router-dom';

import HashTag from '@/components/HashTag';

import * as Styled from './index.styles';

const PostContent = ({ content, hashtags }: Pick<Post, 'hashtags' | 'content'>) => {
  const navigate = useNavigate();

  const handleHashtagClick = (name: string) => {
    navigate(`/hashtag/${name}`);
  };

  return (
    <Styled.ContentContainer>
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
