// @ts-nocheck
import { useState, useEffect, useRef } from 'react';

import useSnackbar from '@/hooks/useSnackbar';

import SNACKBAR_MESSAGE from '@/constants/snackbar';

const useInstallApp = () => {
  const { showSnackbar } = useSnackbar();
  const [installable, setInstallable] = useState(false);
  const deferredPrompt = useRef(null);

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
  };

  return { installable, setInstallable, installApp };
};

export default useInstallApp;
