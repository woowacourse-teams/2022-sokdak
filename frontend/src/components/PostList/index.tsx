import { InfiniteData, InfiniteQueryObserverResult } from 'react-query';
import { useNavigate } from 'react-router-dom';

import { AxiosError } from 'axios';

import PostListItem from '@/components/PostListItem';

import useInfiniteScroll from '@/hooks/useInfiniteScroll';

import * as Styled from './index.styles';

import PATH from '@/constants/path';

interface PostListProps {
  data: InfiniteData<Post> | undefined;
  fetchNextPage: () => Promise<InfiniteQueryObserverResult<Post, AxiosError>>;
}

const PostList = ({ data, fetchNextPage }: PostListProps) => {
  const navigate = useNavigate();
  const { scrollRef } = useInfiniteScroll({ data, proceed: fetchNextPage });

  const handleClickPostItem = (id: number) => {
    navigate(`${PATH.POST}/${id}`);
  };

  return (
    <Styled.Container>
      {data?.pages.map(({ id, title, content, createdAt, likeCount, commentCount, modified, blocked }, index) => (
        <PostListItem
          testid={id}
          blocked={blocked}
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
    </Styled.Container>
  );
};

export default PostList;
