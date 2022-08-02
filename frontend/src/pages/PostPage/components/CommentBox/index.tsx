import { useContext, useReducer } from 'react';

import SnackbarContext from '@/context/Snackbar';

import useReportComment from '@/hooks/queries/comment/useReportComment';

import * as Styled from './index.styles';

import timeConverter from '@/utils/timeConverter';

import ReportModal from '../ReportModal';

const CommentBox = ({ id, nickname, content, createdAt, authorized }: CommentType) => {
  const [isReportModalOpen, handleReportModal] = useReducer(state => !state, false);

  const { mutate: reportComment } = useReportComment({
    onSettled: () => {
      handleReportModal();
    },
  });

  const handleClickReportButton = () => {
    handleReportModal();
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
            <Styled.DeleteButton>삭제</Styled.DeleteButton>
          )}
        </Styled.CommentHeader>
        <Styled.Content>{content}</Styled.Content>
        <Styled.Date>{timeConverter(createdAt)}</Styled.Date>
      </Styled.Container>
      <ReportModal isModalOpen={isReportModalOpen} onClose={handleReportModal} submitReport={submitReportComment} />
    </>
  );
};

export default CommentBox;
