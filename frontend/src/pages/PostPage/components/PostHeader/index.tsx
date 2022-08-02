import { useNavigate } from 'react-router-dom';

import LikeButton from '@/components/LikeButton';

import * as Styled from './index.styles';

import PATH from '@/constants/path';
import timeConverter from '@/utils/timeConverter';

interface PostHeaderProps {
  post: { content: string; title: string; createdAt: string; hashtags: Hashtag[]; authorized: boolean };
  like: { isLiked: boolean; likeCount: number };
  onClickDeleteButton: () => void;
  onClickLikeButton: () => void;
}

const PostHeader = ({ post, like, onClickDeleteButton, onClickLikeButton }: PostHeaderProps) => {
  const navigate = useNavigate();

  return (
    <Styled.HeadContainer>
      <Styled.TitleContainer>
        <Styled.Title>{post.title}</Styled.Title>
      </Styled.TitleContainer>
      {post.authorized && (
        <Styled.PostController>
          <Styled.UpdateButton onClick={() => navigate(PATH.UPDATE_POST, { state: { ...post } })}>
            수정
          </Styled.UpdateButton>
          <Styled.DeleteButton onClick={onClickDeleteButton}>삭제</Styled.DeleteButton>
        </Styled.PostController>
      )}
      <Styled.PostInfo>
        <Styled.Author>익명</Styled.Author>
        <Styled.Date>{timeConverter(post.createdAt)}</Styled.Date>
      </Styled.PostInfo>
      <Styled.LikeButtonContainer>
        <LikeButton {...like} onClick={onClickLikeButton} />
      </Styled.LikeButtonContainer>
    </Styled.HeadContainer>
  );
};

export default PostHeader;
