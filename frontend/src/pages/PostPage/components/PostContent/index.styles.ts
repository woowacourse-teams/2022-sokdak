import styled from '@emotion/styled';

export const Content = styled.p`
  min-height: 370px;
  line-height: 25px;
  white-space: pre-wrap;
  line-break: anywhere;
`;

export const ContentContainer = styled.div<{ hasImage: boolean }>`
  width: 100%;
  min-height: ${props => (props.hasImage ? '120px' : '420px')};
  border-bottom: 1px solid ${props => props.theme.colors.sub};
  margin-bottom: 25px;

  ${Content} {
    min-height: ${props => (props.hasImage ? '70px' : '370px')};
  }
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
