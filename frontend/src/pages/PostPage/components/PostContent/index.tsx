import HashTag from '@/components/HashTag';

import * as Styled from './index.styles';

const PostContent = ({ content, hashtags }: Pick<Post, 'hashtags' | 'content'>) => {
  return (
    <Styled.ContentContainer>
      <Styled.Content>{content}</Styled.Content>
      <Styled.TagContainer>
        {hashtags?.map(({ name }) => (
          <HashTag key={name} name={name} />
        ))}
      </Styled.TagContainer>
    </Styled.ContentContainer>
  );
};

export default PostContent;
