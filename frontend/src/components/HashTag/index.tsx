import * as Styled from './index.styles';

interface HashTagProps {
  className?: string;
  name: string;
  count?: number;
  handleTagClick?: React.MouseEventHandler<HTMLDivElement>;
}

const HashTag = ({ className, name, count, handleTagClick }: HashTagProps) => {
  return (
    <Styled.Container className={className} onClick={handleTagClick}>
      # {name} {count && <Styled.Count>({count})</Styled.Count>}
    </Styled.Container>
  );
};

export default HashTag;
