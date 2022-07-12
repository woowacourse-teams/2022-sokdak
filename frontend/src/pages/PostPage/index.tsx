import { useQuery } from 'react-query';
import { useParams } from 'react-router-dom';

import axios from 'axios';

import Layout from '@/components/@styled/Layout';
import Spinner from '@/components/Spinner';

import * as Styled from './index.styles';

import timeConverter from '@/utils/timeConverter';

const PostPage = () => {
  const { id } = useParams();
  const getPost = () => axios.get(`/posts/${id}`).then(res => res.data);
  const { data, isLoading, isError } = useQuery('post-get', getPost, {});

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
          존재하지 않거나 삭제된 글입니다. <Styled.ListButton to="/">메인 페이지로</Styled.ListButton>
        </Styled.ErrorContainer>
      </Layout>
    );
  }

  const { content, title, localDate } = data;

  return (
    <Layout>
      <Styled.Container>
        <Styled.HeadContainer>
          <Styled.TitleContainer>
            <Styled.Title>{title}</Styled.Title>
          </Styled.TitleContainer>
          <Styled.Date>{timeConverter(localDate)}</Styled.Date>
        </Styled.HeadContainer>

        <Styled.ContentContainer>
          <Styled.Content>{content}</Styled.Content>
        </Styled.ContentContainer>
        <Styled.ListButtonContainer>
          <Styled.ListButton to="/">글 목록</Styled.ListButton>
        </Styled.ListButtonContainer>
      </Styled.Container>
    </Layout>
  );
};

export default PostPage;
