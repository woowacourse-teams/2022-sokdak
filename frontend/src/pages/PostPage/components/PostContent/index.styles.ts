import styled from '@emotion/styled';

export const ContentContainer = styled.div`
  width: 100%;
  min-height: 420px;
  border-bottom: 1px solid ${props => props.theme.colors.sub};
  margin-bottom: 25px;
`;

export const Content = styled.p`
  min-height: 370px;
  line-height: 25px;
  white-space: pre-wrap;
  line-break: anywhere;
`;

export const TagContainer = styled.div`
  min-height: 50px;
  float: left;
  display: flex;
  box-sizing: border-box;
  flex-wrap: wrap;
  gap: 5px;
  row-gap: 10px;
  padding: 15px 0;
`;
