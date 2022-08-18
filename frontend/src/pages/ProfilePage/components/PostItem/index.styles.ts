import styled from '@emotion/styled';

export const Container = styled.div`
  width: 100%;
  height: 100px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  border-bottom: 1px solid ${props => props.theme.colors.gray_400};
  padding: 10px 0;
  cursor: pointer;
`;

export const Title = styled.p`
  font-weight: 700;
  font-size: 15px;
`;

export const Content = styled.p`
  font-size: 14px;
  color: ${props => props.theme.colors.gray_200};
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  word-wrap: break-word;
  line-height: 19px;
  max-height: 57px;
  white-space: pre-wrap;
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
