import * as Styled from './index.styles';

interface SpinnerProps {
  className?: string;
}

const Spinner = ({ className }: SpinnerProps) => {
  return <Styled.Spinner className={className} />;
};

export default Spinner;
