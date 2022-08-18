import * as Styled from './index.styles';

import countFormatter from '@/utils/countFormatter';

interface PostCountInfoProps {
  likeCount: number;
  commentCount: number;
}

const PostCountInfo = ({ likeCount, commentCount }: PostCountInfoProps) => {
  return (
    <Styled.Container>
      <Styled.LikeIcon />
      <Styled.LikeCount>{countFormatter(likeCount)}</Styled.LikeCount>
      <Styled.CommentIcon />
      <Styled.CommentCount>{countFormatter(commentCount)}</Styled.CommentCount>
    </Styled.Container>
  );
};

export default PostCountInfo;
