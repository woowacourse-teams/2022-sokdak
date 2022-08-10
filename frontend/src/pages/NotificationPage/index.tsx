import { useRef } from 'react';

import NotificationItem from './components/NotificationItem';
import Layout from '@/components/@styled/Layout';

import useNotifications from '@/hooks/queries/notification/useNotifications';
import useInfiniteScroll from '@/hooks/useInfiniteScroll';

import * as Styled from './index.styles';

import { convertToTimeType } from '@/utils/timeConverter';

const NotificationPage = () => {
  const typeRef = useRef(-2);

  const { isLoading, isError, data, fetchNextPage } = useNotifications({ storeCode: [3] });
  const { scrollRef } = useInfiniteScroll({ data, proceed: fetchNextPage });

  const timeTypeArr: Array<TimeType> = [...new Set<TimeType>(['오늘', '어제', '이번주', '이번달', '오래전'])];
  if (isError || isLoading) {
    return <></>;
  }

  if (!data) {
    return <></>;
  }

  return (
    <Layout>
      <Styled.NotificationPageContainer>
        <Styled.Title>알람</Styled.Title>
        <Styled.NotificationContainer>
          {data?.pages.map((notice, index) => {
            console.log(convertToTimeType(new Date(notice.createdAt)));
            if (
              typeRef.current !== timeTypeArr.findIndex(type => type === convertToTimeType(new Date(notice.createdAt)))
            ) {
              typeRef.current = timeTypeArr.findIndex(type => type === convertToTimeType(new Date(notice.createdAt)));
              return (
                <>
                  <Styled.NotificationTime>{convertToTimeType(new Date(notice.createdAt))}</Styled.NotificationTime>
                  <NotificationItem
                    key={notice.id}
                    notification={notice}
                    ref={index === data.pages.length - 1 ? scrollRef : null}
                  />
                </>
              );
            }
            return (
              <NotificationItem
                key={notice.id}
                notification={notice}
                ref={index === data.pages.length - 1 ? scrollRef : null}
              />
            );
          })}
        </Styled.NotificationContainer>
      </Styled.NotificationPageContainer>
    </Layout>
  );
};

export default NotificationPage;
