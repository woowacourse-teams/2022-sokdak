import styled from '@emotion/styled';

const Layout = styled.section`
  width: 80%;
  min-width: 375px;
  display: flex;
  justify-content: center;
  box-sizing: border-box;
  margin: auto;

  @media (max-width: 875px) {
    width: 340px;
  }
`;

export default Layout;
