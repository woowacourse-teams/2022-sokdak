import { useNavigate } from 'react-router-dom';

import * as Styled from './index.styles';

import PATH from '@/constants/path';

interface PostProps {
  id: number;
  title: string;
  content: string;
  keyword: string;
  closeModal: () => void;
}

const Post = ({ id, title, content, keyword: keywordBundle, closeModal }: PostProps) => {
  const navigate = useNavigate();
  const keywords = keywordBundle.trim().split(' ');

  const highlight = (target: string) => {
    let result = target;

    keywords.forEach(keyword => {
      if (!target.includes(keyword)) return;

      result = result.replaceAll(keyword, `<span class="highlight">${keyword}</span>`);
    });

    return result;
  };

  const handleClick = () => {
    navigate(`${PATH.POST}/${id}`);
    closeModal();
  };

  return (
    <Styled.Container onClick={handleClick}>
      <Styled.Title
        dangerouslySetInnerHTML={{
          __html: highlight(title),
        }}
      ></Styled.Title>
      <Styled.Content
        dangerouslySetInnerHTML={{
          __html: highlight(content),
        }}
      ></Styled.Content>
      <Styled.URL>{window.location.origin + PATH.POST + `/${id}`}</Styled.URL>
    </Styled.Container>
  );
};

export default Post;
