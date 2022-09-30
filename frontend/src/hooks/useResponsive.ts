import { useEffect, useState } from 'react';

import throttleConstructor from '@/utils/throttle';

const useResponsive = (size: number) => {
  const [isSizeOver, setIsSizeOver] = useState(() => window.innerWidth > size);
  const throttle = throttleConstructor();
  const handleResize = throttle(() => {
    if (size < window.innerWidth) {
      setIsSizeOver(true);
    }
    if (size > window.innerWidth) {
      setIsSizeOver(false);
    }
  }, 100);

  useEffect(() => {
    window.addEventListener('resize', handleResize);
    return () => {
      window.removeEventListener('resize', handleResize);
    };
  }, []);

  return isSizeOver;
};

export default useResponsive;
