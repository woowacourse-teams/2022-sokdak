import styled from '@emotion/styled';

export const Container = styled.div`
  width: 100%;
  height: 100px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  border-bottom: 1px solid ${props => props.theme.colors.gray_400};
  padding: 10px 0;
`;

export const Title = styled.p`
  font-weight: 700;
  font-size: 15px;
`;

export const Content = styled.p`
  font-size: 14px;
  color: ${props => props.theme.colors.gray_200};
`;

export const Information = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
`;

export const CreatedAt = styled.p`
  font-size: 10px;
  color: ${props => props.theme.colors.gray_200};
`;
