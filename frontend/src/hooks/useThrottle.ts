import { useState } from 'react';

const useThrottle = (callback: (...input: unknown[]) => unknown, delay: number) => {
  const [timerID, setTimerID] = useState<NodeJS.Timer | null>(null);

  return function () {
    if (timerID) {
      return;
    }
    callback();
    setTimerID(
      setTimeout(() => {
        setTimerID(null);
      }, delay),
    );
  };
};

export default useThrottle;
