import { HTMLAttributes } from 'react';

import * as Styled from './index.styles';

import HeartImg from '@/assets/images/heart.svg';
import countFormatter from '@/utils/countFormatter';

import { useTheme } from '@emotion/react';

interface LikeButtonProps extends HTMLAttributes<HTMLButtonElement> {
  isLiked: boolean;
  likeCount: number;
}

const LikeButton = ({ isLiked, onClick, likeCount }: LikeButtonProps) => {
  const theme = useTheme();
  const strokeColor = isLiked ? theme.colors.pink_300 : theme.colors.gray_300;
  const fillColor = isLiked ? theme.colors.pink_300 : 'white';

  return (
    <Styled.Button isLiked={isLiked} onClick={onClick}>
      <HeartImg fill={fillColor} stroke={strokeColor} width="16px" height="14px" />
      {countFormatter(likeCount)}
    </Styled.Button>
  );
};

export default LikeButton;
