import { useNavigate } from 'react-router-dom';

import NoResult from '@/pages/SearchedPostPage/components/NoResult';

import Post from './components/Post';

import * as Styled from './index.styles';

import PATH from '@/constants/path';

interface SearchedPostProps {
  posts: Post[];
  totalPostCount: number;
  keyword: string;
  closeModal: () => void;
}

const SearchedPost = ({ posts, totalPostCount, keyword, closeModal }: SearchedPostProps) => {
  const navigate = useNavigate();

  const viewMore = () => {
    closeModal();
    navigate(`${PATH.SEARCH_POST}/${keyword.trim().replaceAll(' ', '+')}`);
  };

  return (
    <Styled.Container>
      <Styled.Header>
        <Styled.ResultCount>{totalPostCount}개의 검색 결과</Styled.ResultCount>
        {!!totalPostCount && <Styled.ViewMore onClick={viewMore}>더보기</Styled.ViewMore>}
      </Styled.Header>
      {posts.length ? (
        posts.map(({ id, title, content }) => (
          <Post key={id} id={id} title={title} content={content} keyword={keyword} closeModal={closeModal} />
        ))
      ) : (
        <NoResult />
      )}
    </Styled.Container>
  );
};

export default SearchedPost;
