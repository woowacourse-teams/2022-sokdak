import * as Styled from './index.styles';

interface HashTagProps {
  id?: number;
  name: string;
}

const HashTag = ({ name }: HashTagProps) => {
  return <Styled.Container># {name}</Styled.Container>;
};

export default HashTag;
