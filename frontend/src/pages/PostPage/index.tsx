import Layout from '@/components/@styled/Layout';
import Spinner from '@/components/Spinner';
import timeConverter from '@/utils/timeConverter';
import axios from 'axios';
import { useQuery } from 'react-query';
import { useParams } from 'react-router-dom';
import * as Styled from './index.styles';

const PostPage = () => {
  const { id } = useParams();
  const getPost = () => axios.get(`/posts/${id}`).then(res => res.data);
  const { data, isLoading } = useQuery('post-get', getPost, {});

  if (isLoading) {
    return (
      <Layout>
        <Styled.SpinnerContainer>
          <Spinner />
        </Styled.SpinnerContainer>
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
