import { useReducer } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import CommentList from './components/CommentList';
import PostContent from './components/PostContent';
import PostHeader from './components/PostHeader';
import Sidebar from './components/Sidebar';
import Layout from '@/components/@styled/Layout';
import ConfirmModal from '@/components/ConfirmModal';
import Spinner from '@/components/Spinner';

import useLike from '@/hooks/queries/likes/useLike';
import useDeletePost from '@/hooks/queries/post/useDeletePost';
import usePost from '@/hooks/queries/post/usePost';
import usePosts from '@/hooks/queries/post/usePosts';
import useResponsive from '@/hooks/useResponsive';

import * as Styled from './index.styles';

import PATH from '@/constants/path';

import { AD } from '@/dummy';

const HOT_BORAD_ID = 1;
const POST_COUNT = 5;

const PostPage = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [isConfirmModalOpen, handleConfirmModal] = useReducer(state => !state, false);

  const { data, isLoading, isError } = usePost({
    storeCode: id!,
    options: {
      staleTime: 1000 * 20,
    },
  });
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

  const handleLikeButton = () => {
    putLike({ id: id! });
  };

  if (isLoading) {
    return (
      <Layout>
        <Styled.SpinnerContainer>
          <Spinner />
        </Styled.SpinnerContainer>
      </Layout>
    );
  }

  if (isError) {
    return (
      <Layout>
        <Styled.ErrorContainer>
          <Spinner />
          존재하지 않거나 삭제된 글입니다. <Styled.ListButton to={PATH.HOME}>메인 페이지로</Styled.ListButton>
        </Styled.ErrorContainer>
      </Layout>
    );
  }
  if (data?.blocked) {
    return (
      <Layout>
        <Styled.BlockContainer>
          <p>다량의 신고로 인해</p>
          <p> 블라인드 처리 된 게시물 입니다.</p>
          <Styled.BackButton
            onClick={() => {
              navigate(-1);
            }}
          >
            이전 페이지로 돌아가기
          </Styled.BackButton>
        </Styled.BlockContainer>
      </Layout>
    );
  }

  return (
    <Layout>
      <Styled.Container>
        <Styled.PostContainer>
          <PostHeader post={data!} onClickDeleteButton={handleConfirmModal} onClickLikeButton={handleLikeButton} />
          {hasImage && <Styled.Image src={process.env.IMAGE_API_URL + data.imageName} alt={data.imageName} />}
          <PostContent content={data?.content!} hashtags={data?.hashtags!} hasImage={hasImage} />
          <CommentList id={id!} />
        </Styled.PostContainer>

        {isDesktop && hotPosts && (
          <Styled.SideContainer>
            <Sidebar
              title="실시간 인기글"
              items={hotPosts.pages.map(item => ({ name: item.title, url: `${PATH.POST}/${item.id}` }))!}
            />
            <Styled.ADSidebar title="AD" items={AD} domain="external" />
          </Styled.SideContainer>
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
