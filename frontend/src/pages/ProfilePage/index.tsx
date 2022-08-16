import { useContext, useReducer, useRef, useState } from 'react';

import PostItem from './components/PostItem';
import Layout from '@/components/@styled/Layout';

import AuthContext from '@/context/Auth';

import useUpdateNickname from '@/hooks/queries/member/useUpdateNickname';
import useMyPosts from '@/hooks/queries/profile/useMyPosts';
import useSnackbar from '@/hooks/useSnackbar';

import * as Styled from './index.styles';

import SNACKBAR_MESSAGE from '@/constants/snackbar';

const ProfilePage = () => {
  const { showSnackbar } = useSnackbar();
  const { username, setUserName } = useContext(AuthContext);
  const [nickname, setNickname] = useState(username);
  const [disabled, handleDisabled] = useReducer(state => !state, true);

  const size = 3;
  const [currentPage, setCurrentPage] = useState(1);
  const { data } = useMyPosts({
    storeCode: [size, currentPage],
  });

  const nicknameRef = useRef<HTMLInputElement>(null);
  const { mutate } = useUpdateNickname({
    onSuccess: () => {
      setUserName(nickname);
      showSnackbar(SNACKBAR_MESSAGE.SUCCESS_UPDATE_NICKNAME);
    },
  });

  const handleClick = (e: React.FormEvent) => {
    e.preventDefault();

    if (!disabled) {
      mutate({ nickname });
    }

    handleDisabled();
  };

  return (
    <Layout>
      <Styled.Container>
        <Styled.Avatar>
          <Styled.Panda />
        </Styled.Avatar>
        <Styled.NicknameField>
          <Styled.Nickname
            value={nickname}
            onChange={e => setNickname(e.target.value)}
            maxLength={16}
            disabled={disabled}
            ref={nicknameRef}
            size={nickname.length}
            placeholder="닉네임을 입력해주세요."
            required
          />
          <Styled.UpdateButton onClick={handleClick}>{disabled ? '수정' : '완료'}</Styled.UpdateButton>
        </Styled.NicknameField>
        <Styled.PostField>
          <Styled.PostListHeader>내가 쓴 글</Styled.PostListHeader>
          {data && data.posts && data.posts.length !== 0 ? (
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
          ) : (
            <Styled.EmptyPostList>아직 작성된 글이 없어요 !</Styled.EmptyPostList>
          )}
        </Styled.PostField>
      </Styled.Container>
    </Layout>
  );
};

export default ProfilePage;
