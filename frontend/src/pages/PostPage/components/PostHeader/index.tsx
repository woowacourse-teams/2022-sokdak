import { useContext, useReducer } from 'react';
import { useQueryClient } from 'react-query';
import { useNavigate } from 'react-router-dom';

import LikeButton from '@/components/LikeButton';

import SnackbarContext from '@/context/Snackbar';

import useReportPost from '@/hooks/queries/post/useReportPost';

import * as Styled from './index.styles';

import PATH from '@/constants/path';
import { MUTATION_KEY } from '@/constants/queries';
import SNACKBAR_MESSAGE from '@/constants/snackbar';
import timeConverter from '@/utils/timeConverter';

import ReportModal from '../ReportModal';

interface PostHeaderProps {
  post: {
    id: number;
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
  const { showSnackbar } = useContext(SnackbarContext);
  const [isReportModalOpen, handleReportModal] = useReducer(state => !state, false);
  const queryClient = useQueryClient();

  const { mutate: submitReport } = useReportPost({
    onSuccess: () => {
      showSnackbar(SNACKBAR_MESSAGE.SUCCESS_REPORT_POST);
      handleReportModal();
    },
    onError: (err, variables, context) => {
      if (typeof queryClient.getMutationDefaults(MUTATION_KEY.REPORT_POST)?.onError === 'function') {
        queryClient.getMutationDefaults(MUTATION_KEY.REPORT_POST)?.onError!(err, variables, context);
      }

      showSnackbar(err.message);
      handleReportModal();
    },
  });

  const handleReportModalOpen = () => {
    handleReportModal();
  };

  const handleSubmitReport = (message: string) => {
    submitReport({ id: post.id, message });
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
      <ReportModal isModalOpen={isReportModalOpen} onClose={handleReportModal} submitReport={handleSubmitReport} />
    </Styled.HeadContainer>
  );
};

export default PostHeader;
