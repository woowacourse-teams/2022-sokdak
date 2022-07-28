import ReactDOM from 'react-dom';

import * as Styled from './index.styles';

interface ConfirmModalProps {
  title: string;
  notice: string;
  handleCancel: () => void;
  handleConfirm: () => void;
}

const ConfirmModal = ({ title, notice, handleCancel, handleConfirm }: ConfirmModalProps) => {
  return (
    <>
      {ReactDOM.createPortal(
        <>
          <Styled.Modal>
            <Styled.Title>{title}</Styled.Title>
            <Styled.Notice>{notice}</Styled.Notice>
            <Styled.ButtonContainer>
              <Styled.CancelButton onClick={handleCancel}>취소</Styled.CancelButton>
              <Styled.ConfirmButton onClick={handleConfirm}>확인</Styled.ConfirmButton>
            </Styled.ButtonContainer>
          </Styled.Modal>
          <Styled.Dimmer onClick={handleCancel}></Styled.Dimmer>
        </>,
        document.getElementById('confirm-modal')!,
      )}
    </>
  );
};

export default ConfirmModal;
