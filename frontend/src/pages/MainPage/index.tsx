import * as Styled from './index.styles';
import PostListItem from '@/components/PostListItem';
import FAB from '@/components/FAB';
import { useNavigate } from 'react-router-dom';
import Layout from '@/components/@styled/Layout';
import axios from 'axios';
import { useRef, useState, useEffect } from 'react';
import { useInfiniteQuery } from 'react-query';
import Spinner from '@/components/Spinner';

const MainPage = () => {
  const navigate = useNavigate();
  const scrollRef = useRef(null);
  const [postList, setPostList] = useState<Post[]>([]);
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

  const getPosts = async ({ pageParam = 0 }) => {
    const { data } = await axios.get(`/posts?size=5&page=${pageParam}`);

    return {
      ...data,
      nextPage: pageParam + 1,
    };
  };

  const { isLoading, data, fetchNextPage } = useInfiniteQuery('posts-getByPage', getPosts, {
    enabled: false,
    getNextPageParam: ({ lastPage, nextPage }) => (lastPage ? undefined : nextPage),
  });

  useEffect(() => {
    if (!data) {
      fetchNextPage();
    }
  }, []);

  useEffect(() => {
    if (data) {
      setPostList(data.pages.reduce((prev, { posts }) => prev.concat(posts), []));
    }
  }, [data]);

  useEffect(() => {
    if (scrollRef.current) {
      io.observe(scrollRef.current);
    }
  }, [postList]);

  return (
    <Layout>
      <Styled.PostListContainer>
        {postList.map(({ id, title, localDate, content }: Post, index) => (
          <PostListItem
            title={title}
            localDate={localDate}
            content={content}
            key={id}
            handleClick={e => handleClickPostItem(id)}
            ref={index === postList.length - 1 ? scrollRef : null}
          />
        ))}
        {isLoading && <Spinner />}
      </Styled.PostListContainer>
      <FAB handleClick={handleClickFAB} />
    </Layout>
  );
};

export default MainPage;
