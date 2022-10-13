import { useEffect, useRef, useState } from 'react';

const useCarousel = () => {
  const ref = useRef(null);
  const [isEnd, setIsEnd] = useState(false);

  const observer = new IntersectionObserver(
    entries => {
      entries.forEach(entry => {
        if (!entry.isIntersecting) return;
        setIsEnd(true);
      });
    },
    { threshold: 1.0 },
  );

  useEffect(() => {
    observer.observe(ref.current!);
  }, []);

  return { ref, isEnd, setIsEnd };
};

export default useCarousel;
