import { useState } from 'react';
import { Link } from 'react-router-dom';

import Layout from '@/components/@styled/Layout';

import * as Styled from './index.styles';

import PATH from '@/constants/path';

const NotFoundPage = () => {
  const [isVisible, setIsVisible] = useState(false);

  return (
    <Layout>
      <Styled.Container>
        <Styled.ErrorCode>404</Styled.ErrorCode>
        <Styled.ErrorMessage>페이지를 찾을 수 없습니다.</Styled.ErrorMessage>
        <Link to={PATH.HOME}>
          <Styled.Panda onMouseEnter={() => setIsVisible(true)} onMouseLeave={() => setIsVisible(false)} />
        </Link>
        {isVisible && <Styled.ErrorMessage css={Styled.fixed}>메인 페이지로 이동</Styled.ErrorMessage>}
      </Styled.Container>
    </Layout>
  );
};

export default NotFoundPage;
