import { useReducer } from 'react';

import ConfirmModal from '@/components/ConfirmModal';

import useDeleteComment from '@/hooks/queries/comment/useDeleteComment';
import useReportComment from '@/hooks/queries/comment/useReportComment';

import * as Styled from './index.styles';

import timeConverter from '@/utils/timeConverter';

import ReportModal from '../ReportModal';

const CommentBox = ({ id, nickname, content, createdAt, authorized }: CommentType) => {
  const [isReportModalOpen, handleReportModal] = useReducer(state => !state, false);
  const [isDeleteModalOpen, handleDeleteModal] = useReducer(state => !state, false);

  const { mutate: deleteComment } = useDeleteComment();
  const { mutate: reportComment } = useReportComment({
    onSettled: () => {
      handleReportModal();
    },
  });

  const handleClickReportButton = () => {
    handleReportModal();
  };

  const handleClickDeleteButton = () => {
    handleDeleteModal();
  };

  const submitReportComment = (message: string) => {
    reportComment({ id, message });
  };

  return (
    <>
      <Styled.Container>
        <Styled.CommentHeader>
          <Styled.Nickname>{nickname}</Styled.Nickname>
          {authorized ? (
            <Styled.ReportButton onClick={handleClickReportButton}>🚨</Styled.ReportButton>
          ) : (
            <Styled.DeleteButton onClick={handleClickDeleteButton}>삭제</Styled.DeleteButton>
          )}
        </Styled.CommentHeader>
        <Styled.Content>{content}</Styled.Content>
        <Styled.Date>{timeConverter(createdAt)}</Styled.Date>
      </Styled.Container>
      {isDeleteModalOpen && (
        <ConfirmModal
          title="삭제"
          notice="해당 글을 삭제하시겠습니까?"
          handleCancel={handleClickDeleteButton}
          handleConfirm={() => {
            deleteComment({ id });
          }}
        />
      )}
      <ReportModal isModalOpen={isReportModalOpen} onClose={handleReportModal} submitReport={submitReportComment} />
    </>
  );
};

export default CommentBox;
