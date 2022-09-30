import useResponsive from '@/hooks/useResponsive';

import * as Styled from './index.styles';

import Panda from '@/assets/images/panda_logo.svg';

const Banner = () => {
  const isOver = useResponsive(1100);
  const isTabletSizeOver = useResponsive(875);

  return (
    <>
      {isTabletSizeOver ? (
        <Styled.BannerContainer>
          <Styled.LeftSide>
            <Panda width={'200px'} />
            <Styled.BannerText>
              현재 <Styled.HighlightText>속닥속닥</Styled.HighlightText>에서
              <br /> 인기있는 게시물을
              <br /> 만나보세요 !
            </Styled.BannerText>
          </Styled.LeftSide>
          <Styled.HashTagsContainer>
            <Styled.HashtagContainer x={-140} y={-50}>
              <Styled.Hashtag>
                <Styled.HashtagText>속닥속다고ㅅ갇거ㅏ거다ㅓㄱㅁㄴㅇㄹㅁㄴㅇㄹㅁㄴㅇㄹㄴㄴㄹ</Styled.HashtagText>
              </Styled.Hashtag>
            </Styled.HashtagContainer>
            <Styled.HashtagContainer x={125} y={-50}>
              <Styled.Hashtag>
                <Styled.HashtagText>공식</Styled.HashtagText>
              </Styled.Hashtag>
            </Styled.HashtagContainer>
            <Styled.HashtagContainer x={60} y={80}>
              <Styled.Hashtag>
                <Styled.HashtagText>공식</Styled.HashtagText>
              </Styled.Hashtag>
            </Styled.HashtagContainer>
            <Styled.HashtagContainer x={isOver ? 180 : 100} y={30}>
              <Styled.Hashtag>
                <Styled.HashtagText>공식</Styled.HashtagText>
              </Styled.Hashtag>
            </Styled.HashtagContainer>
            <Styled.HashtagContainer x={0} y={0}>
              <Styled.Hashtag>
                <Styled.HashtagText>공식</Styled.HashtagText>
              </Styled.Hashtag>
            </Styled.HashtagContainer>
            <Styled.HashtagContainer x={-125} y={40}>
              <Styled.Hashtag>
                <Styled.HashtagText>공식</Styled.HashtagText>
              </Styled.Hashtag>
            </Styled.HashtagContainer>
          </Styled.HashTagsContainer>
        </Styled.BannerContainer>
      ) : (
        <></>
      )}
    </>
  );
};

export default Banner;
