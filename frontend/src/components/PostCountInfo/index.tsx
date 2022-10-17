import * as Styled from './index.styles';

import countFormatter from '@/utils/countFormatter';

interface PostCountInfoProps {
  likeCount: number;
  commentCount: number;
  ariaHidden: boolean;
}

const PostCountInfo = ({ likeCount, commentCount, ariaHidden = false }: PostCountInfoProps) => {
  return (
    <Styled.Container aria-hidden={ariaHidden} aria-label={`좋아요 ${likeCount}개 댓글 수 ${commentCount}개`}>
      <Styled.LikeIcon />
      <Styled.LikeCount>{countFormatter(likeCount)}</Styled.LikeCount>
      <Styled.CommentIcon />
      <Styled.CommentCount>{countFormatter(commentCount)}</Styled.CommentCount>
    </Styled.Container>
  );
};

export default PostCountInfo;
