import * as Styled from './index.styles';

interface SubmitButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  children: React.ReactNode;
}

const SubmitButton = ({ children, onClick }: SubmitButtonProps) => {
  return <Styled.Button onClick={onClick}>{children}</Styled.Button>;
};

export default SubmitButton;
