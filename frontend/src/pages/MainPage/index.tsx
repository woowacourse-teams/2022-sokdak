import * as Styled from './index.styles';
import PostListItem from '@/components/PostListItem';
import FAB from '@/components/FAB';
import { useNavigate } from 'react-router-dom';
import Layout from '@/components/@styled/Layout';
import { postList } from '@/dummy';

const MainPage = () => {
  const navigate = useNavigate();
  const handleClickFAB = () => {
    navigate('/post/write');
  };

  return (
    <Layout>
      <Styled.PostListContainer>
        {postList.map(({ id, title, localDate, content }: Post) => (
          <PostListItem title={title} localDate={localDate} content={content} key={id} />
        ))}
      </Styled.PostListContainer>
      <FAB handleClick={handleClickFAB} />
    </Layout>
  );
};

export default MainPage;
