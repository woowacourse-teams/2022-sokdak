import { useNavigate } from 'react-router-dom';

import useSearchHashtags from '@/hooks/queries/hashtag/useSearchHashtags';

import * as Styled from './index.styles';

import PATH from '@/constants/path';

const HASHTAG_POSITION: { x: number; y: number }[] = [
  { x: -140, y: -50 },
  { x: 125, y: -50 },
  { x: 60, y: 80 },
  { x: 140, y: 30 },
  { x: 0, y: 0 },
  { x: -125, y: 40 },
];

const Banner = () => {
  const navigate = useNavigate();

  const { data } = useSearchHashtags({
    storeCode: [10, ''],
  });

  const handleHashTagClick = (name: string) => navigate(`${PATH.HASHTAG}/${name}`);

  return (
    <Styled.BannerContainer>
      <Styled.LeftSide>
        <Styled.BannerText>
          현재 인기있는
          <br /> 게시물을 만나보세요!
        </Styled.BannerText>
      </Styled.LeftSide>

      <Styled.RightSide>
        {HASHTAG_POSITION.map((item, i) => {
          if (!data?.data.hashtags || data?.data.hashtags.length <= i) return;
          return (
            <Styled.HashtagContainer
              x={item.x}
              y={item.y}
              key={data.data.hashtags[i].name}
              onClick={() => handleHashTagClick(data?.data.hashtags[i].name)}
            >
              <Styled.Hashtag>
                <Styled.HashtagText>{data.data.hashtags[i].name}</Styled.HashtagText>
              </Styled.Hashtag>
            </Styled.HashtagContainer>
          );
        })}
      </Styled.RightSide>
    </Styled.BannerContainer>
  );
};

export default Banner;
