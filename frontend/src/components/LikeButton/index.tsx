import { HTMLAttributes } from 'react';

import * as Styled from './index.styles';

import HeartImg from '@/assets/images/heart.svg';

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
      <HeartImg fill={fillColor} stroke={strokeColor} width="20px" height="20px" /> {likeCount}
    </Styled.Button>
  );
};

export default LikeButton;
