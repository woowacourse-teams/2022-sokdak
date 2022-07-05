import * as Styled from './index.styles';

interface FABProps {
  handleClick: () => void;
}

const FAB = ({ handleClick }: FABProps) => {
  return <Styled.Container onClick={handleClick}>+</Styled.Container>;
};

export default FAB;
