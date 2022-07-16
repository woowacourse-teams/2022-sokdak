import * as Styled from './index.styles';

interface SubmitButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  children: React.ReactNode;
}

const SubmitButton = ({ children, onClick, disabled }: SubmitButtonProps) => {
  return (
    <Styled.Button onClick={onClick} disabled={disabled}>
      {children}
    </Styled.Button>
  );
};

export default SubmitButton;
