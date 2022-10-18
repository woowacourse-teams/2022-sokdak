import { useRef } from 'react';

export default function useDebounce(callback: () => void, delay: number) {
  // State and setters for debounced value
  const timerID = useRef<NodeJS.Timeout | undefined>(undefined);

  return function () {
    clearTimeout(timerID.current);

    timerID.current = setTimeout(() => {
      callback();
    }, delay);
  };
}
