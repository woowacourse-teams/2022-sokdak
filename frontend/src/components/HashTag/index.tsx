import * as Styled from './index.styles';

interface HashTagProps {
  name: string;
  handleTagClick?: React.MouseEventHandler<HTMLDivElement>;
}

const HashTag = ({ name, handleTagClick }: HashTagProps) => {
  return <Styled.Container onClick={handleTagClick}># {name}</Styled.Container>;
};

export default HashTag;
