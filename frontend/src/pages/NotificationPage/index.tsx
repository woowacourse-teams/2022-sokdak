import React from 'react';

import NotificationItem from './components/NotificationItem';
import Layout from '@/components/@styled/Layout';

import useNotifications from '@/hooks/queries/notification/useNotifications';
import useInfiniteScroll from '@/hooks/useInfiniteScroll';

import * as Styled from './index.styles';

import { SIZE } from '@/constants/api';
import { convertToTimeType } from '@/utils/timeConverter';

const NotificationPage = () => {
  let type = -2;
  const { isLoading, isError, data, fetchNextPage } = useNotifications({ storeCode: [SIZE.NOTIFICATION_LOAD] });
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
        <Styled.Title>알림</Styled.Title>
        {data.pages.length > 0 ? (
          <Styled.NotificationContainer>
            {data?.pages.map((notice, index) => {
              if (type !== timeTypeArr.findIndex(type => type === convertToTimeType(new Date(notice.createdAt)))) {
                type = timeTypeArr.findIndex(type => type === convertToTimeType(new Date(notice.createdAt)));
                return (
                  <React.Fragment key={notice.id}>
                    <Styled.NotificationTime>{convertToTimeType(new Date(notice.createdAt))}</Styled.NotificationTime>
                    <NotificationItem notification={notice} ref={index === data.pages.length - 1 ? scrollRef : null} />
                  </React.Fragment>
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
        ) : (
          <Styled.EmptyContainer>최근 알림이 존재하지 않습니다.</Styled.EmptyContainer>
        )}
      </Styled.NotificationPageContainer>
    </Layout>
  );
};

export default NotificationPage;
