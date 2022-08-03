import {
  createContext,
  ReactNode,
  SetStateAction,
  useContext,
  useState,
  Dispatch,
  useRef,
  useEffect,
  useCallback,
} from 'react';

import * as Styled from './index.styles';

interface DropdownProps {
  children: ReactNode;
}
const DropdownContext = createContext<{ open: boolean; setOpen: Dispatch<SetStateAction<boolean>> } | undefined>(
  undefined,
);

const Dropdown = ({ children }: DropdownProps) => {
  const [open, setOpen] = useState(false);

  const ref = useRef<HTMLDivElement>(null);

  const handleClick = useCallback((e: MouseEvent) => {
    if (ref.current && !ref.current?.contains(e.target as Node)) {
      setOpen(false);
    }
  }, []);

  useEffect(() => {
    document.addEventListener('click', handleClick);
    return () => {
      document.removeEventListener('click', handleClick);
    };
  }, []);

  return (
    <DropdownContext.Provider value={{ open, setOpen }}>
      <Styled.DropdownContainer ref={ref}>{children}</Styled.DropdownContainer>
    </DropdownContext.Provider>
  );
};

const Trigger = ({ children }: DropdownProps) => {
  const dropdown = useContext(DropdownContext);
  if (!dropdown) {
    throw new Error('');
  }

  const handleTrigger = () => {
    dropdown.setOpen(prev => !prev);
  };

  return <Styled.DropdownTrigger onClick={handleTrigger}>{children}</Styled.DropdownTrigger>;
};

const OptionList = ({ children }: DropdownProps) => {
  const dropdown = useContext(DropdownContext);
  if (!dropdown) {
    throw new Error('');
  }

  return <Styled.DropdownList>{dropdown.open ? children : null}</Styled.DropdownList>;
};

Dropdown.Trigger = Trigger;
Dropdown.OptionList = OptionList;

export default Dropdown;
