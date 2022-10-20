import styled from '@emotion/styled';

export const Container = styled.div`
  width: fit-content;
  font-family: 'BMHANNAAir', 'Noto Sans KR';
  font-size: 1.3rem;
  line-height: 1.7rem;
  margin-top: 100px;
  color: ${props => props.theme.colors.gray_200};

  @media (max-width: 875px) {
    font-size: 1rem;
  }
`;

export const Highlight = styled.span`
  font-family: 'BMHANNAPro', 'Noto Sans KR';
`;
