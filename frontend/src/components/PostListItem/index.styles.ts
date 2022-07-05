import styled from '@emotion/styled';

export const Container = styled.div`
  width: 340px;
  height: 180px;
  display: flex;
  flex-direction: column;
  padding: 20px 16px;
  box-shadow: 0px 1px 7px rgba(0, 0, 0, 0.13);
  border-radius: 5px;
  gap: 14px;
`;

export const TitleContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
`;

export const Title = styled.p`
  margin: 0;
  font-size: 24px;
  font-family: 'BMHANNAPro';
`;

export const Date = styled.span`
  font-size: 10px;
  color: ${props => props.theme.colors.gray_200};
`;

export const ContentContainer = styled.div`
  padding: 1em;
  border-radius: 5px;
  background-color: ${props => props.theme.colors.gray_100};
`;

export const Content = styled.p`
  margin: 0;
  font-size: 18px;
  line-height: 25px;

  display: -webkit-box;
  max-width: 308px;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
  overflow: hidden;
`;
