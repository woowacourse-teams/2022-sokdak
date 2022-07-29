import * as Styled from './index.styles';

interface HashTagProps {
  className?: string | undefined;
  name: string;
  handleTagClick?: React.MouseEventHandler<HTMLDivElement>;
}

const HashTag = ({ className, name, handleTagClick }: HashTagProps) => {
  return (
    <Styled.Container className={className} onClick={handleTagClick}>
      # {name}
    </Styled.Container>
  );
};

export default HashTag;
