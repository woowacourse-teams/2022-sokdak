import * as Styled from './index.styles';

interface FABProps {
  handleClick: () => void;
}

const FAB = ({ handleClick }: FABProps) => {
  return (
    <Styled.ButtonContainer>
      <Styled.Container onClick={handleClick}>+</Styled.Container>
    </Styled.ButtonContainer>
  );
};

export default FAB;
