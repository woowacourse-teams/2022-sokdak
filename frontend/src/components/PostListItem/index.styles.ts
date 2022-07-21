import Comment from '@/assets/images/comment.svg';
import Heart from '@/assets/images/heart.svg';

import styled from '@emotion/styled';

export const Container = styled.div`
  width: 340px;
  height: 200px;
  display: flex;
  flex-direction: column;
  padding: 20px 16px;
  box-shadow: 0px 1px 7px rgba(0, 0, 0, 0.13);
  border-radius: 5px;
  gap: 14px;
  cursor: pointer;
`;

export const HeadContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
`;

export const TitleContainer = styled.div`
  display: flex;
  align-items: flex-end;
  gap: 7px;
`;

export const Title = styled.p<{ isModified: boolean }>`
  font-size: 24px;
  font-family: 'BMHANNAPro';

  overflow: hidden;
  text-overflow: ellipsis;
  max-width: ${({ isModified }) => (isModified ? '225px' : '260px')};
  white-space: nowrap;
`;

export const Modified = styled.span`
  color: ${props => props.theme.colors.gray_200};
  font-size: 10px;
  min-width: 35px;
`;

export const Date = styled.span`
  font-size: 10px;
  color: ${props => props.theme.colors.gray_200};
  width: fit-content;
  text-align: end;
`;

export const ContentContainer = styled.div`
  padding: 1em;
  border-radius: 5px;
  background-color: ${props => props.theme.colors.gray_100};
  height: 110px;
`;

export const Content = styled.p`
  font-size: 18px;
  line-height: 25px;
  white-space: pre-wrap;
  display: -webkit-box;
  max-width: 308px;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
  overflow: hidden;
`;

export const PostInfoContainer = styled.div`
  align-self: flex-end;
  display: flex;
  justify-content: space-between;
`;

export const LikeIcon = styled(Heart)`
  fill: ${props => props.theme.colors.pink_300};
  stroke: ${props => props.theme.colors.pink_300};
  width: 16px;
  height: 14px;
  margin: 0 4px 0 10px;
`;

export const LikeCount = styled.span`
  color: ${props => props.theme.colors.pink_300};
`;

export const CommentIcon = styled(Comment)`
  width: 16px;
  height: 15px;
  margin: 0 4px 0 10px;
`;

export const CommentCount = styled.span`
  color: ${props => props.theme.colors.gray_200};
`;
