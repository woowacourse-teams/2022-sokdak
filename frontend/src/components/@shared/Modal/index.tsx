import { ReactNode } from 'react';
import ReactDOM from 'react-dom';

import * as Styled from './index.styles';

interface ModalProps {
  isModalOpen: boolean;
  children: ReactNode;
  handleCancel: () => void;
}

const Modal = ({ isModalOpen, children, handleCancel }: ModalProps) => {
  return (
    <>
      {isModalOpen && (
        <>
          {ReactDOM.createPortal(
            <>
              <Styled.Modal>{children}</Styled.Modal>
              <Styled.Dimmer onClick={handleCancel}></Styled.Dimmer>
            </>,
            document.getElementById('confirm-modal')!,
          )}
        </>
      )}
    </>
  );
};

export default Modal;
