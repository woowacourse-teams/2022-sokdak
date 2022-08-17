import { useContext, useEffect, useReducer, useRef, useState } from 'react';

import PostItem from './components/PostItem';
import Layout from '@/components/@styled/Layout';

import AuthContext from '@/context/Auth';

import useUpdateNickname from '@/hooks/queries/member/useUpdateNickname';
import useMyPosts from '@/hooks/queries/profile/useMyPosts';
import useSnackbar from '@/hooks/useSnackbar';

import * as Styled from './index.styles';

import SNACKBAR_MESSAGE from '@/constants/snackbar';

const SIZE = 3;
const OFFSET = 1.5;

const ProfilePage = () => {
  const { showSnackbar } = useSnackbar();
  const [currentPage, setCurrentPage] = useState(1);
  const { username, setUserName } = useContext(AuthContext);
  const [nickname, setNickname] = useState(username);
  const [disabled, handleDisabled] = useReducer(state => !state, true);
  const nicknameRef = useRef<HTMLInputElement>(null);

  const { data } = useMyPosts({
    storeCode: [SIZE, currentPage],
  });
  const { mutate } = useUpdateNickname({
    onSuccess: () => {
      setUserName(nickname);
      showSnackbar(SNACKBAR_MESSAGE.SUCCESS_UPDATE_NICKNAME);
    },
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    if (!disabled) {
      mutate({ nickname });
    }

    handleDisabled();
  };

  useEffect(() => {
    if (!disabled) {
      nicknameRef.current?.focus();
    }
  }, [disabled]);

  return (
    <Layout>
      <Styled.Container>
        <Styled.Avatar>
          <Styled.Panda />
        </Styled.Avatar>
        <Styled.NicknameField onSubmit={handleSubmit}>
          <Styled.Nickname
            value={nickname}
            onChange={e => setNickname(e.target.value)}
            onInvalid={(e: React.FormEvent<HTMLInputElement>) => {
              e.preventDefault();
              showSnackbar(e.currentTarget.placeholder);
            }}
            maxLength={16}
            disabled={disabled}
            ref={nicknameRef}
            size={nickname.length * OFFSET}
            placeholder="닉네임을 입력해주세요."
            required
          />
          <Styled.UpdateButton>{disabled ? '수정' : '완료'}</Styled.UpdateButton>
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
