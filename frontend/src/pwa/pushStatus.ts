interface PushStatus {
  pushSupport: boolean;
  pushSubscription: PushSubscription | null;
  serviceWorkerRegistration?: ServiceWorkerRegistration;
  notificationPermission?: 'granted' | 'default' | 'denied';
}

export interface KeyResponse {
  key: string;
}

const pushStatus: PushStatus = {
  pushSupport: false,
  pushSubscription: null,
  serviceWorkerRegistration: undefined,
  notificationPermission: undefined,
};

export const setPushStatus = async (registration: ServiceWorkerRegistration) => {
  pushStatus.pushSupport = !!registration.pushManager;
  pushStatus.pushSubscription = await registration.pushManager.getSubscription();
  pushStatus.serviceWorkerRegistration = registration;
  pushStatus.notificationPermission = Notification.permission;
};

export default pushStatus;
