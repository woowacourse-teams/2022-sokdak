import { useNavigate } from 'react-router-dom';

import * as Styled from './index.styles';

import PATH from '@/constants/path';

interface SearchedHashtagProps {
  hashtags: Hashtag[];
  closeModal: () => void;
}

const SearchedHashtag = ({ hashtags, closeModal }: SearchedHashtagProps) => {
  const navigate = useNavigate();

  const handleClick = (name: string) => {
    closeModal();
    navigate(`${PATH.HASHTAG}/${name}`);
  };

  return (
    <Styled.Container>
      {hashtags.map(({ name, count }) => (
        <Styled.Hashtag key={name} name={name} count={count} handleTagClick={() => handleClick(name)} />
      ))}
    </Styled.Container>
  );
};

export default SearchedHashtag;
