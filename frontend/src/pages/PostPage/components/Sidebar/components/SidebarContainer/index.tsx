import { HTMLAttributes, PropsWithChildren, useEffect, useLayoutEffect, useRef, useState } from 'react';

import useThrottle from '@/hooks/useThrottle';

import * as Styled from './index.styles';

const SidebarContainer = ({ children, className }: PropsWithChildren<HTMLAttributes<HTMLDivElement>>) => {
  const [position, setPosition] = useState(document.documentElement.scrollTop);
  const moveSidebar = useThrottle(() => setPosition(document.documentElement.scrollTop), 50);

  const sideBarContainerRef = useRef<HTMLDivElement>(null);
  const sideBarRef = useRef<HTMLDivElement>(null);
  const [sideBarContainerHeight, setSideBarContainerHeight] = useState(0);
  const [sideBarHeight, setSideBarHeight] = useState(0);

  useLayoutEffect(() => {
    setSideBarContainerHeight(sideBarContainerRef.current?.clientHeight!);
    setSideBarHeight(sideBarRef.current?.clientHeight!);
  }, []);

  useEffect(() => {
    window.addEventListener('scroll', moveSidebar);

    return () => window.removeEventListener('scroll', moveSidebar);
  }, []);

  return (
    <Styled.SidebarContainer ref={sideBarContainerRef}>
      <Styled.Container
        position={position}
        className={className}
        height={sideBarContainerHeight - sideBarHeight}
        ref={sideBarRef}
      >
        {children}
      </Styled.Container>
    </Styled.SidebarContainer>
  );
};

export default SidebarContainer;
