import { useReducer } from 'react';
import { useNavigate } from 'react-router-dom';

import LikeButton from '@/components/LikeButton';

import * as Styled from './index.styles';

import PATH from '@/constants/path';
import timeConverter from '@/utils/timeConverter';

import ReportModal from '../ReportModal';

interface PostHeaderProps {
  post: {
    content: string;
    title: string;
    createdAt: string;
    hashtags: Omit<Hashtag, 'count'>[];
    authorized: boolean;
    nickname: string;
  };
  like: { isLiked: boolean; likeCount: number };
  onClickDeleteButton: () => void;
  onClickLikeButton: () => void;
}

const PostHeader = ({ post, like, onClickDeleteButton, onClickLikeButton }: PostHeaderProps) => {
  const navigate = useNavigate();
  const [isReportModalOpen, handleReportModal] = useReducer(state => !state, false);

  const handleReportModalOpen = () => {
    handleReportModal();
  };

  return (
    <Styled.HeadContainer>
      <Styled.TitleContainer>
        <Styled.Title>{post.title}</Styled.Title>
      </Styled.TitleContainer>
      <Styled.PostController>
        {post.authorized ? (
          <>
            <Styled.UpdateButton onClick={() => navigate(PATH.UPDATE_POST, { state: { ...post } })}>
              수정
            </Styled.UpdateButton>
            <Styled.DeleteButton onClick={onClickDeleteButton}>삭제</Styled.DeleteButton>
          </>
        ) : (
          <Styled.ReportButton onClick={handleReportModalOpen}>🚨</Styled.ReportButton>
        )}
      </Styled.PostController>
      <Styled.PostInfo>
        <Styled.Author>{post.nickname}</Styled.Author>
        <Styled.Date>{timeConverter(post.createdAt)}</Styled.Date>
      </Styled.PostInfo>
      <Styled.LikeButtonContainer>
        <LikeButton {...like} onClick={onClickLikeButton} />
      </Styled.LikeButtonContainer>
      <ReportModal isModalOpen={isReportModalOpen} onClose={handleReportModal} />
    </Styled.HeadContainer>
  );
};

export default PostHeader;
