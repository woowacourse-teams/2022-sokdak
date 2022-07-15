import { useNavigate, useParams } from 'react-router-dom';

import Layout from '@/components/@styled/Layout';
import Spinner from '@/components/Spinner';

import usePost from '@/hooks/queries/post/usePost';

import * as Styled from './index.styles';

import PATH from '@/constants/path';
import timeConverter from '@/utils/timeConverter';

const PostPage = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const { data, isLoading, isError } = usePost({ storeCode: id! });

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
            <Styled.DeleteButton>삭제</Styled.DeleteButton>
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
      </Styled.Container>
    </Layout>
  );
};

export default PostPage;
