import { useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import Layout from '@/components/@styled/Layout';
import FAB from '@/components/FAB';
import PostListItem from '@/components/PostListItem';
import Spinner from '@/components/Spinner';

import usePosts from '@/hooks/queries/post/usePosts';

import * as Styled from './index.styles';

const MainPage = () => {
  const navigate = useNavigate();
  const scrollRef = useRef(null);
  const { isLoading, data, fetchNextPage } = usePosts({ storeCode: [3] });

  const io = new IntersectionObserver(
    (entries, io) => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          io.unobserve(entry.target);
          fetchNextPage();
        }
      });
    },
    { threshold: 0.7 },
  );

  const handleClickFAB = () => {
    navigate('/post/write');
  };

  const handleClickPostItem = (id: number) => {
    navigate(`post/${id}`);
  };

  useEffect(() => {
    if (!data) {
      fetchNextPage();
    }

    if (scrollRef.current) {
      io.observe(scrollRef.current);
    }
  }, [data]);

  return (
    <Layout>
      <Styled.PostListContainer>
        {data?.pages.map(({ id, title, localDate, content }, index) => (
          <PostListItem
            title={title}
            localDate={localDate}
            content={content}
            key={id}
            handleClick={e => handleClickPostItem(id)}
            ref={index === data.pages.length - 1 ? scrollRef : null}
          />
        ))}
        {isLoading && <Spinner />}
      </Styled.PostListContainer>
      <FAB handleClick={handleClickFAB} />
    </Layout>
  );
};

export default MainPage;
