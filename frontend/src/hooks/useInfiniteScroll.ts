import { useEffect, useRef } from 'react';
import { InfiniteData } from 'react-query';

interface UseInfiniteScrollProps {
  data: InfiniteData<unknown> | undefined;
  proceed: () => void;
}

const useInfiniteScroll = ({ data, proceed }: UseInfiniteScrollProps) => {
  const scrollRef = useRef(null);

  const io = new IntersectionObserver(
    (entries, io) => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          io.unobserve(entry.target);
          proceed();
        }
      });
    },
    { threshold: 0.7 },
  );

  useEffect(() => {
    if (!data) {
      proceed();
    }

    if (scrollRef.current) {
      io.observe(scrollRef.current);
    }
  }, [data]);

  return {
    scrollRef,
  };
};

export default useInfiniteScroll;
