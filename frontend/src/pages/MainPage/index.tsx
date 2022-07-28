import { useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import Layout from '@/components/@styled/Layout';
import FAB from '@/components/FAB';
import PostListItem from '@/components/PostListItem';
import Spinner from '@/components/Spinner';

import usePosts from '@/hooks/queries/post/usePosts';

import * as Styled from './index.styles';

import PATH from '@/constants/path';

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
    navigate(PATH.CREATE_POST);
  };

  const handleClickPostItem = (id: number) => {
    navigate(`${PATH.POST}/${id}`);
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
        {data?.pages.map(({ id, title, content, createdAt, likeCount, commentCount, modified }, index) => (
          <PostListItem
            testid={id}
            title={title}
            content={content}
            createdAt={createdAt}
            likeCount={likeCount}
            commentCount={commentCount}
            modified={modified}
            key={id}
            handleClick={() => handleClickPostItem(id)}
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
