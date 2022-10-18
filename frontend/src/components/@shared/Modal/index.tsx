import { PropsWithChildrenC } from 'sokdak-util-types';

import ReactDOM from 'react-dom';

import * as Styled from './index.styles';

interface ModalProps {
  isModalOpen: boolean;
  handleCancel: () => void;
}

const Modal = ({ isModalOpen, children, handleCancel }: PropsWithChildrenC<ModalProps>) => {
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
