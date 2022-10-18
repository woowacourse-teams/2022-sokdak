import { useState, useEffect, useRef } from 'react';

import useSnackbar from '@/hooks/useSnackbar';

import SNACKBAR_MESSAGE from '@/constants/snackbar';

interface BeforeInstallPromptEvent extends Event {
  readonly platforms: Array<string>;
  readonly userChoice: Promise<{
    outcome: 'accepted' | 'dismissed';
    platform: string;
  }>;
  prompt(): Promise<void>;
}

declare global {
  interface WindowEventMap {
    beforeinstallprompt: BeforeInstallPromptEvent;
  }
}

const useInstallApp = () => {
  const { showSnackbar } = useSnackbar();
  const [installable, setInstallable] = useState(false);
  const deferredPrompt = useRef<BeforeInstallPromptEvent | null>(null);

  useEffect(() => {
    window.addEventListener('beforeinstallprompt', e => {
      e.preventDefault();
      deferredPrompt.current = e;
      setInstallable(!!deferredPrompt.current);
    });
  }, []);

  const installApp = () => {
    if (!deferredPrompt.current) {
      showSnackbar(SNACKBAR_MESSAGE.FAIL_INSTALL);
      setInstallable(false);
      return;
    }

    deferredPrompt.current.prompt();
    setInstallable(false);
  };

  return { installable, setInstallable, installApp };
};

export default useInstallApp;
