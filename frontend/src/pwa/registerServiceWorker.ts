import { setPushStatus } from './pushStatus';

const registerServiceWorker = () => {
  const isSupported = process.env.MODE !== 'LOCAL:MSW' && 'serviceWorker' in navigator;

  if (!isSupported) return;

  window.addEventListener('load', () => {
    navigator.serviceWorker
      .register('/service-worker.js')
      .then(registration => {
        setPushStatus(registration);
      })
      .catch(() => {});
  });
};

export default registerServiceWorker;
