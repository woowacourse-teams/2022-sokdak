import { StateAndAction } from 'sokdak-util-types';

import { MouseEventHandler, useEffect, useRef } from 'react';

import * as Styled from './index.styles';

interface PromptProps extends StateAndAction<boolean, 'visible'> {
  message: string;
  confirmText: string;
  cancelText: string;
  confirm: MouseEventHandler<HTMLButtonElement>;
  cancel: MouseEventHandler<HTMLButtonElement>;
}

const Prompt = ({ message, confirmText, cancelText, confirm, cancel, setVisible }: PromptProps) => {
  const containerRef = useRef<HTMLDivElement>(null);
  const handleOutsideClick = (e: MouseEvent) => {
    if (!containerRef.current || !(e.target instanceof Node)) return;
    if (!containerRef.current.contains(e.target)) setVisible(false);
  };

  useEffect(() => {
    window.addEventListener('click', handleOutsideClick);
    return () => window.removeEventListener('click', handleOutsideClick);
  }, []);

  return (
    <Styled.Container ref={containerRef}>
      <Styled.Message>{message}</Styled.Message>
      <Styled.ButtonContainer>
        <Styled.Confirm onClick={confirm}>{confirmText}</Styled.Confirm>
        <Styled.Cancel onClick={cancel}>{cancelText}</Styled.Cancel>
      </Styled.ButtonContainer>
    </Styled.Container>
  );
};

export default Prompt;
