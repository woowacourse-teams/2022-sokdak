import { StateAndAction } from 'sokdak-util-types';

import { MouseEventHandler, useEffect, useRef } from 'react';

import * as Styled from './index.styles';

import { STORAGE_KEY } from '@/constants/localStorage';

interface PromptProps extends StateAndAction<boolean, 'visible'> {
  message: string;
  confirmText: string;
  cancelText: string;
  hideText?: string;
  confirm: MouseEventHandler<HTMLButtonElement>;
  cancel: MouseEventHandler<HTMLButtonElement>;
}

const Prompt = ({
  message,
  confirmText,
  cancelText,
  hideText = '😴 다시 보지 않기',
  confirm,
  cancel,
  setVisible,
}: PromptProps) => {
  const containerRef = useRef<HTMLDivElement>(null);

  const handleOutsideClick = (e: MouseEvent) => {
    if (!containerRef.current || !(e.target instanceof Node)) return;
    if (!containerRef.current.contains(e.target)) setVisible(false);
  };

  const hide = () => {
    localStorage.setItem(STORAGE_KEY.INSTALL_PROMPT_HIDE, 'hide');
    setVisible(false);
  };

  useEffect(() => {
    if (localStorage.getItem(STORAGE_KEY.INSTALL_PROMPT_HIDE)) {
      setVisible(false);
      return;
    }

    window.addEventListener('click', handleOutsideClick);
    return () => window.removeEventListener('click', handleOutsideClick);
  }, []);

  return (
    <Styled.Container ref={containerRef}>
      <Styled.Message>{message}</Styled.Message>
      <Styled.ButtonContainer>
        <Styled.Controller>
          <Styled.Cancel onClick={cancel}>{cancelText}</Styled.Cancel>
          <Styled.Confirm onClick={confirm}>{confirmText}</Styled.Confirm>
        </Styled.Controller>
        <Styled.Hide onClick={hide}>{hideText}</Styled.Hide>
      </Styled.ButtonContainer>
    </Styled.Container>
  );
};

export default Prompt;
