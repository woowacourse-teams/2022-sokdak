import Modal from '@/components/@shared/Modal';

import * as Styled from './index.styles';

interface ReportModal {
  isModalOpen: boolean;
  onClose: () => void;
}
const ReportModal = ({ isModalOpen, onClose }: ReportModal) => {
  return (
    <Modal isModalOpen={isModalOpen} handleCancel={onClose}>
      <Styled.ReportModalContainer>
        <Styled.Title>신고🚨</Styled.Title>
        <Styled.Notice>신고 사유를 적어주세요.</Styled.Notice>
        <Styled.Message />
        <Styled.ButtonContainer>
          <Styled.CancelButton onClick={onClose}>취소</Styled.CancelButton>
          <Styled.ConfirmButton>확인</Styled.ConfirmButton>
        </Styled.ButtonContainer>
      </Styled.ReportModalContainer>
    </Modal>
  );
};

export default ReportModal;
