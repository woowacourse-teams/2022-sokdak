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
      <div ref={ref}>{children}</div>
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

  return <button onClick={handleTrigger}>{children}</button>;
};

const OptionList = ({ children }: DropdownProps) => {
  const dropdown = useContext(DropdownContext);
  if (!dropdown) {
    throw new Error('');
  }

  return <ul>{dropdown.open ? children : null}</ul>;
};

Dropdown.Trigger = Trigger;
Dropdown.OptionList = OptionList;

export default Dropdown;
