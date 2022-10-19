import { useReducer } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import CommentList from './components/CommentList';
import PostContent from './components/PostContent';
import PostHeader from './components/PostHeader';
import Sidebar from './components/Sidebar';
import SidebarContainer from './components/Sidebar/components/SidebarContainer';
import Layout from '@/components/@styled/Layout';
import ConfirmModal from '@/components/ConfirmModal';
import Spinner from '@/components/Spinner';

import useLike from '@/hooks/queries/likes/useLike';
import useDeletePost from '@/hooks/queries/post/useDeletePost';
import usePost from '@/hooks/queries/post/usePost';
import usePosts from '@/hooks/queries/post/usePosts';
import useDebounce from '@/hooks/useDebounce';
import useResponsive from '@/hooks/useResponsive';

import * as Styled from './index.styles';

import AD from '@/constants/AD';
import PATH from '@/constants/path';

const HOT_BORAD_ID = 1;
const POST_COUNT = 5;

const PostPage = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [isConfirmModalOpen, handleConfirmModal] = useReducer(state => !state, false);

  const { data, isError } = usePost({
    storeCode: id!,
    options: {
      staleTime: 1000 * 20,
    },
  });

  const handleLikeButton = useDebounce(() => {
    if (data?.boardId) putLike({ id: id! });
  }, 100);

  const hasImage = !!(data && data.imageName !== '');
  const { mutate: putLike } = useLike({});
  const { mutate: deletePost } = useDeletePost({
    onSuccess: () => {
      handleConfirmModal();
      navigate(-1);
    },
  });

  const isDesktop = useResponsive(875);
  const { data: hotPosts } = usePosts({
    storeCode: [HOT_BORAD_ID, POST_COUNT],
    options: {
      enabled: isDesktop,
      staleTime: 1000 * 20,
    },
  });

  if (isError || !data) {
    return (
      <Styled.NotFound>
        <Spinner />
        <Styled.ErrorMessage>존재하지 않거나 삭제된 글입니다. </Styled.ErrorMessage>
        <Styled.HandlingButtonContainer>
          <Styled.MainButton onClick={() => navigate(PATH.HOME)}>메인으로</Styled.MainButton>
          <Styled.BackButton onClick={() => navigate(-1)}>이전 페이지</Styled.BackButton>
        </Styled.HandlingButtonContainer>
      </Styled.NotFound>
    );
  }

  if (data.blocked) {
    return (
      <Styled.Block>
        <Styled.ErrorMessage>
          다량의 신고로 인해
          <br />
          블라인드 처리 된 게시물 입니다.
        </Styled.ErrorMessage>
        <Styled.HandlingButtonContainer>
          <Styled.MainButton onClick={() => navigate(PATH.HOME)}>메인으로</Styled.MainButton>
          <Styled.BackButton onClick={() => navigate(-1)}>이전 페이지</Styled.BackButton>
        </Styled.HandlingButtonContainer>
      </Styled.Block>
    );
  }

  return (
    <Layout>
      <Styled.Container>
        <Styled.PostContainer>
          <PostHeader post={data} onClickDeleteButton={handleConfirmModal} onClickLikeButton={handleLikeButton} />
          {hasImage && <Styled.Image src={process.env.IMAGE_API_URL + data.imageName} alt={data.imageName} />}
          <PostContent content={data.content} hashtags={data.hashtags} hasImage={hasImage} />
          <CommentList id={id!} />
        </Styled.PostContainer>

        {isDesktop && hotPosts && (
          <SidebarContainer>
            <Sidebar
              title="✨ 실시간 인기글"
              items={hotPosts.pages.map(item => ({ name: item.title, url: `${PATH.POST}/${item.id}` }))!}
            />
            <Sidebar title="📢 홍보" items={AD} domain="external" />
          </SidebarContainer>
        )}

        {isConfirmModalOpen && (
          <ConfirmModal
            title="삭제"
            notice="해당 글을 삭제하시겠습니까?"
            handleCancel={handleConfirmModal}
            handleConfirm={() => deletePost(id!)}
          />
        )}
      </Styled.Container>
    </Layout>
  );
};

export default PostPage;
