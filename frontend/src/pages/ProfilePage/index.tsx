import { useState } from 'react';

import PostItem from './components/PostItem';
import Layout from '@/components/@styled/Layout';

import useMyPosts from '@/hooks/queries/profile/useMyPosts';

import * as Styled from './index.styles';

const ProfilePage = () => {
  const size = 3;
  const [currentPage, setCurrentPage] = useState(1);

  const { data } = useMyPosts({
    storeCode: [size, currentPage],
  });

  return (
    <Layout>
      <Styled.Container>
        <Styled.Avatar>
          <Styled.Panda />
        </Styled.Avatar>
        <Styled.NicknameField>
          <Styled.Nickname>속닥속닥</Styled.Nickname>
          <Styled.UpdateButton>수정</Styled.UpdateButton>
        </Styled.NicknameField>
        <Styled.PostField>
          <Styled.PostListHeader>내가 쓴 글</Styled.PostListHeader>
          {data && data.posts && (
            <>
              <Styled.PostList>
                {data.posts.map(post => (
                  <PostItem key={post.id} {...post} />
                ))}
              </Styled.PostList>
              <Styled.Pagination
                lastPage={data.totalPageCount}
                currentPage={currentPage}
                setCurrentPage={setCurrentPage}
              />
            </>
          )}
        </Styled.PostField>
      </Styled.Container>
    </Layout>
  );
};

export default ProfilePage;
