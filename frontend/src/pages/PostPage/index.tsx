import { useReducer } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import CommentList from './components/CommentList';
import Layout from '@/components/@styled/Layout';
import ConfirmModal from '@/components/ConfirmModal';
import Spinner from '@/components/Spinner';

import useDeletePost from '@/hooks/queries/post/useDeletePost';
import usePost from '@/hooks/queries/post/usePost';

import * as Styled from './index.styles';

import PATH from '@/constants/path';
import timeConverter from '@/utils/timeConverter';

const PostPage = () => {
  const navigate = useNavigate();

  const { id } = useParams();
  const { data, isLoading, isError } = usePost({ storeCode: id! });
  const [isConfirmModalOpen, handleConfirmModal] = useReducer(state => !state, false);
  const { mutate: deletePost } = useDeletePost({
    onSuccess: () => {
      handleConfirmModal();
      navigate(-1);
    },
  });

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

  const { content, title, createdAt } = data!;

  return (
    <Layout>
      <Styled.Container>
        <Styled.HeadContainer>
          <Styled.TitleContainer>
            <Styled.Title>{title}</Styled.Title>
          </Styled.TitleContainer>
          <Styled.PostController>
            <Styled.UpdateButton onClick={() => navigate(PATH.UPDATE_POST, { state: { id, title, content } })}>
              수정
            </Styled.UpdateButton>
            <Styled.DeleteButton onClick={handleConfirmModal}>삭제</Styled.DeleteButton>
          </Styled.PostController>
          <Styled.PostInfo>
            <Styled.Author>익명</Styled.Author>
            <Styled.Date>{timeConverter(createdAt)}</Styled.Date>
          </Styled.PostInfo>
        </Styled.HeadContainer>

        <Styled.ContentContainer>
          <Styled.Content>{content}</Styled.Content>
        </Styled.ContentContainer>
        <Styled.ListButtonContainer>
          <Styled.ListButton to={PATH.HOME}>글 목록</Styled.ListButton>
        </Styled.ListButtonContainer>
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
