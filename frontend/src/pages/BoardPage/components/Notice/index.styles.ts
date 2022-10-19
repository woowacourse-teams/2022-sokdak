import styled from '@emotion/styled';

export const Container = styled.div`
  width: 100%;
  display: flex;
  margin: 10px 0 30px 0;
  font-family: 'Noto Sans KR';
  flex-direction: column;

  @media (max-width: 875px) {
    font-size: 13px;
  }
`;

export const Text = styled.p`
  line-height: 25px;
`;

export const Highlight = styled.span`
  font-weight: 800;
`;

export const Link = styled.a`
  cursor: pointer;
  color: ${props => props.theme.colors.gray_200};
  border-bottom: 1px solid ${props => props.theme.colors.gray_200};
  margin-top: 2px;
  width: fit-content;

  :hover {
    color: ${props => props.theme.colors.sub};
    border-bottom: 1px solid ${props => props.theme.colors.sub};
  }
`;

export const LinkContainer = styled.div`
  display: flex;
  gap: 10px;
  margin-top: 25px;
`;
