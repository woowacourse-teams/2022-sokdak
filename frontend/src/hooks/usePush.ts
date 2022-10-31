import { useState } from 'react';

import { AxiosResponse } from 'axios';

import authFetcher from '@/apis';
import PUSH from '@/constants/push';
import urlB64ToUint8Array from '@/utils/urlB64ToUint8Array';

import pushStatus, { KeyResponse } from '@/pwa/pushStatus';

// TODO: API 요청 분리
const getPublicKey = async () => {
  const { data } = await authFetcher.get<KeyResponse, AxiosResponse<KeyResponse>>('/subscription/key');

  return data.key;
};
const postSubscription = async (subscription: PushSubscription) => {
  await authFetcher.post('/subscription', subscription);
};
const deleteSubscription = async () => {
  await authFetcher.delete('/subscription');
};

const usePush = () => {
  const [isSubscribing, setIsSubscribing] = useState(!!pushStatus.pushSubscription);
  const [isLoading, setIsLoading] = useState(false);

  const subscribe = () => {
    if (!pushStatus.pushSupport) return;

    Notification.requestPermission().then(permission => {
      pushStatus.notificationPermission = permission;

      if (permission !== 'granted') return;

      registerPushManager();
    });
  };

  const unsubscribe = async () => {
    if (!pushStatus.pushSubscription) return;

    // await deleteSubscription();
    pushStatus.pushSubscription.unsubscribe();
    pushStatus.pushSubscription = null;
    setIsSubscribing(false);
  };

  const registerPushManager = async () => {
    // const publicKey = await getPublicKey();
    setIsLoading(true);
    const publicKey = 'BBVcLIvhXwML4YQ76rlxp-rAp3Lq11HC53yzfBGKEzy9HmlGsMV0M_eq6_mTpHIazIYoS8YUZRnk9uJIBEpIGho';
    const option = {
      userVisibleOnly: true,
      applicationServerKey: urlB64ToUint8Array(publicKey),
    };

    navigator.serviceWorker.ready.then(registration => {
      registration.pushManager
        .subscribe(option)
        .then(async subscription => {
          // await postSubscription(subscription);
          pushStatus.pushSubscription = subscription;

          registration.showNotification(PUSH.START_SUBSCRIBE_TITLE, {
            body: PUSH.START_SUBSCRIBE_BODY,
          });
        })
        .then(() => {
          setIsSubscribing(true);
        })
        .catch(() => {
          pushStatus.pushSubscription = null;
        })
        .finally(() => {
          setIsLoading(false);
        });
    });
  };

  return {
    isSubscribing,
    isPushSupport: pushStatus.pushSupport,
    isLoading,
    subscribe,
    unsubscribe,
  };
};

export default usePush;
