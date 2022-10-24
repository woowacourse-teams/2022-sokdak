import { RefObject, useEffect, useLayoutEffect, useState } from 'react';

import useThrottle from '@/hooks/useThrottle';

const useAnimateContainer = ({
  parentContainer,
  childContainer,
}: {
  parentContainer: RefObject<HTMLDivElement>;
  childContainer: RefObject<HTMLDivElement>;
}) => {
  const [position, setPosition] = useState(document.documentElement.scrollTop);
  const moveSidebar = useThrottle(() => setPosition(document.documentElement.scrollTop), 50);

  const [sideBarContainerHeight, setSideBarContainerHeight] = useState(0);
  const [sideBarHeight, setSideBarHeight] = useState(0);

  useLayoutEffect(() => {
    setSideBarContainerHeight(parentContainer.current?.clientHeight!);
    setSideBarHeight(childContainer.current?.clientHeight!);
  }, []);

  useEffect(() => {
    window.addEventListener('scroll', moveSidebar);

    return () => window.removeEventListener('scroll', moveSidebar);
  }, []);

  return { movementDegree: Math.min(sideBarContainerHeight - sideBarHeight, position) };
};

export default useAnimateContainer;
