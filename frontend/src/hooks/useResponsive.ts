import { useEffect, useState } from 'react';

import useThrottle from './useThrottle';

const useResponsive = (size: number) => {
  const [isSizeOver, setIsSizeOver] = useState(() => window.innerWidth > size);
  const handleResize = useThrottle(() => {
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
