import React, { useRef } from 'react';

import Modal from '@/components/@shared/Modal';

import * as Styled from './index.styles';

interface ReportModal {
  isModalOpen: boolean;
  onClose: () => void;
  submitReport: (text: string) => void;
}
const ReportModal = ({ isModalOpen, onClose, submitReport }: ReportModal) => {
  const messageRef = useRef<HTMLTextAreaElement>(null);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (messageRef.current) {
      submitReport(messageRef.current.value);
      messageRef.current.value = '';
    }
  };

  return (
    <Modal isModalOpen={isModalOpen} handleCancel={onClose}>
      <Styled.ReportModalContainer onSubmit={handleSubmit}>
        <Styled.Title>신고🚨</Styled.Title>
        <Styled.Notice>신고 사유를 적어주세요.</Styled.Notice>
        <Styled.Message ref={messageRef} />
        <Styled.ButtonContainer>
          <Styled.CancelButton onClick={onClose}>취소</Styled.CancelButton>
          <Styled.ConfirmButton>확인</Styled.ConfirmButton>
        </Styled.ButtonContainer>
      </Styled.ReportModalContainer>
    </Modal>
  );
};

export default ReportModal;
