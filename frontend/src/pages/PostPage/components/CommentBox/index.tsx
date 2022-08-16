import { useReducer } from 'react';

import ConfirmModal from '@/components/ConfirmModal';

import useDeleteComment from '@/hooks/queries/comment/useDeleteComment';
import useLikeComment from '@/hooks/queries/comment/useLikeComment';
import useReportComment from '@/hooks/queries/comment/useReportComment';

import * as Styled from './index.styles';

import HeartImg from '@/assets/images/heart.svg';
import countFormatter from '@/utils/countFormatter';
import timeConverter from '@/utils/timeConverter';

import ReplyForm from '../ReplyForm';
import ReportModal from '../ReportModal';
import { useTheme } from '@emotion/react';

const Mode = {
  COMMENTS: 'comments',
  REPLIES: 'replies',
} as const;

interface CommentBoxProps extends CommentType {
  blocked: boolean;
  postWriter: boolean;
  mode?: typeof Mode[keyof typeof Mode];
  className?: string;
}

const CommentBox = ({
  id,
  nickname,
  content,
  createdAt,
  authorized,
  blocked,
  postWriter,
  mode = Mode.COMMENTS,
  likeCount,
  className,
  like,
}: CommentBoxProps) => {
  const [isReportModalOpen, handleReportModal] = useReducer(state => !state, false);
  const [isDeleteModalOpen, handleDeleteModal] = useReducer(state => !state, false);
  const [isReplyFormOpen, handleReplyForm] = useReducer(state => !state, false);
  const theme = useTheme();
  const strokeColor = like ? theme.colors.pink_300 : theme.colors.gray_300;
  const fillColor = like ? theme.colors.pink_300 : 'white';

  const { mutate: deleteComment } = useDeleteComment();
  const { mutate: reportComment } = useReportComment({
    onSettled: () => {
      handleReportModal();
    },
  });
  const { mutate: likeComment } = useLikeComment();

  const handleClickReportButton = () => {
    handleReportModal();
  };

  const handleClickDeleteButton = () => {
    handleDeleteModal();
  };

  const submitReportComment = (message: string) => {
    reportComment({ id, message });
  };

  const handleLikeButton = () => {
    likeComment({ id });
  };

  if (!content) {
    return <Styled.EmptyComment>작성자에 의해 삭제된 댓글 입니다.</Styled.EmptyComment>;
  }

  if (blocked) {
    return <Styled.EmptyComment>신고에 의해 블라인드 처리되었습니다.</Styled.EmptyComment>;
  }

  return (
    <>
      <Styled.Container className={className}>
        <Styled.CommentHeader>
          <Styled.Nickname>
            {nickname} {postWriter && <Styled.PostWriter>작성자</Styled.PostWriter>}
          </Styled.Nickname>
          <Styled.ButtonContainer>
            {mode === Mode.COMMENTS && <Styled.ReplyButton onClick={handleReplyForm}>답글</Styled.ReplyButton>}
            {authorized ? (
              <Styled.DeleteButton onClick={handleClickDeleteButton}>삭제</Styled.DeleteButton>
            ) : (
              <Styled.ReportButton onClick={handleClickReportButton}>🚨</Styled.ReportButton>
            )}
          </Styled.ButtonContainer>
        </Styled.CommentHeader>
        <Styled.Content>{content}</Styled.Content>
        <Styled.Footer>
          <Styled.Date>{timeConverter(createdAt!)}</Styled.Date>
          <Styled.LikeContainer isLiked={like} onClick={handleLikeButton}>
            <HeartImg fill={fillColor} stroke={strokeColor} width="12px" height="11px" />
            {countFormatter(likeCount)}
          </Styled.LikeContainer>
        </Styled.Footer>
      </Styled.Container>

      {isReplyFormOpen && <ReplyForm commentId={id} />}
      {isDeleteModalOpen && (
        <ConfirmModal
          title="삭제"
          notice="해당 댓글을 삭제하시겠습니까?"
          handleCancel={handleClickDeleteButton}
          handleConfirm={() => deleteComment({ id })}
        />
      )}
      <ReportModal isModalOpen={isReportModalOpen} onClose={handleReportModal} submitReport={submitReportComment} />
    </>
  );
};

export default CommentBox;
