import { useReducer, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import CommentList from './components/CommentList';
import PostHeader from './components/PostHeader';
import Layout from '@/components/@styled/Layout';
import ConfirmModal from '@/components/ConfirmModal';
import HashTag from '@/components/HashTag';
import Spinner from '@/components/Spinner';

import useLike from '@/hooks/queries/likes/useLike';
import useDeletePost from '@/hooks/queries/post/useDeletePost';
import usePost from '@/hooks/queries/post/usePost';

import * as Styled from './index.styles';

import PATH from '@/constants/path';

const PostPage = () => {
  const navigate = useNavigate();
  const { id } = useParams();

  const [like, setLike] = useState({
    isLiked: true,
    likeCount: 0,
  });

  const [isConfirmModalOpen, handleConfirmModal] = useReducer(state => !state, false);
  const { data, isLoading, isError } = usePost({
    storeCode: id!,
    options: {
      onSuccess: data => {
        setLike({ isLiked: data.like, likeCount: data.likeCount });
      },
    },
  });

  const { mutate: putLike } = useLike({
    onSuccess: data => {
      setLike({ isLiked: data.data.like, likeCount: data.data.likeCount });
    },
  });

  const { mutate: deletePost } = useDeletePost({
    onSuccess: () => {
      handleConfirmModal();
      navigate(-1);
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
        <Styled.SpinnerContainer>
          <Spinner />
        </Styled.SpinnerContainer>
        <Styled.ErrorContainer>
          존재하지 않거나 삭제된 글입니다. <Styled.ListButton to={PATH.HOME}>메인 페이지로</Styled.ListButton>
        </Styled.ErrorContainer>
      </Layout>
    );
  }

  const { content, hashtags } = data!;

  return (
    <Layout>
      <Styled.Container>
        <PostHeader
          post={data!}
          like={like}
          onClickDeleteButton={handleConfirmModal}
          onClickLikeButton={handleLikeButton}
        />

        <Styled.ContentContainer>
          <Styled.Content>{content}</Styled.Content>
          <Styled.TagContainer>
            {hashtags?.map(({ name }) => (
              <HashTag key={name} name={name} />
            ))}
          </Styled.TagContainer>
        </Styled.ContentContainer>
        <CommentList id={id!} />
      </Styled.Container>

      {isConfirmModalOpen && (
        <ConfirmModal
          title="삭제"
          notice="해당 글을 삭제하시겠습니까?"
          handleCancel={handleConfirmModal}
          handleConfirm={() => deletePost(id!)}
        />
      )}
    </Layout>
  );
};

export default PostPage;
