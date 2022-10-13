import { useNavigate } from 'react-router-dom';

import useHashtags from '@/hooks/queries/hashtag/useHashtags';
import useResponsive from '@/hooks/useResponsive';

import * as Styled from './index.styles';

import PATH from '@/constants/path';

const TRANSLATE_OBJ: { x: number; y: number }[] = [
  { x: -140, y: -50 },
  { x: 125, y: -50 },
  { x: 60, y: 80 },
  { x: 180, y: 30 },
  { x: 0, y: 0 },
  { x: -125, y: 40 },
];

const Banner = () => {
  const isOver = useResponsive(1100);
  const navigate = useNavigate();

  const { data } = useHashtags({
    storeCode: [10, ''],
  });

  const handleHashTagClick = (name: string) => {
    navigate(`${PATH.HASHTAG}/${name}`);
  };

  return (
    <Styled.BannerContainer>
      <Styled.LeftSide>
        <Styled.PandaContainer>
          <Styled.PandaLogo />
        </Styled.PandaContainer>
        <Styled.BannerText>
          현재 <Styled.HighlightText>속닥속닥</Styled.HighlightText>에서
          <br /> 인기있는 게시물을
          <br /> 만나보세요!
        </Styled.BannerText>
      </Styled.LeftSide>
      <Styled.HashTagsContainer>
        {TRANSLATE_OBJ.map((item, idx) => {
          if (!data?.data.hashtags || data?.data.hashtags.length <= idx) return;
          return (
            <Styled.HashtagContainer
              x={idx === 3 && !isOver ? 100 : item.x}
              y={item.y}
              key={data.data.hashtags[idx].name}
              onClick={() => {
                handleHashTagClick(data?.data.hashtags[idx].name);
              }}
            >
              <Styled.Hashtag>
                <Styled.HashtagText>{data.data.hashtags[idx].name}</Styled.HashtagText>
              </Styled.Hashtag>
            </Styled.HashtagContainer>
          );
        })}
      </Styled.HashTagsContainer>
    </Styled.BannerContainer>
  );
};

export default Banner;
