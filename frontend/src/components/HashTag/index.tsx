import * as Styled from './index.styles';

interface HashTagProps {
  id?: number;
  name: string;
  handleTagClick?: () => void;
}

const HashTag = ({ name, handleTagClick }: HashTagProps) => {
  return <Styled.Container onClick={handleTagClick}># {name}</Styled.Container>;
};

export default HashTag;
