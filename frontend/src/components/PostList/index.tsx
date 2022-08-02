import { useRef, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import FAB from '@/components/FAB';
import PostListItem from '@/components/PostListItem';
import Spinner from '@/components/Spinner';

import usePosts from '@/hooks/queries/post/usePosts';

import * as Styled from './index.styles';

import PATH from '@/constants/path';

const PostList = () => {
  const navigate = useNavigate();
  const { id } = useParams();

  const scrollRef = useRef(null);
  const { isLoading, data, fetchNextPage } = usePosts({ storeCode: [id!, 3] });

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
    <>
      <Styled.Container>
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
        <FAB handleClick={handleClickFAB} />
      </Styled.Container>
    </>
  );
};

export default PostList;
