import { HTMLAttributes, PropsWithChildren, useEffect, useState } from 'react';

import useThrottle from '@/hooks/useThrottle';

import * as Styled from './index.styles';

const SidebarContainer = ({ children, className }: PropsWithChildren<HTMLAttributes<HTMLDivElement>>) => {
  const [position, setPosition] = useState(document.documentElement.scrollTop);
  const moveSidebar = useThrottle(() => setPosition(document.documentElement.scrollTop), 50);

  useEffect(() => {
    window.addEventListener('scroll', moveSidebar);

    return () => window.removeEventListener('scroll', moveSidebar);
  }, []);

  return (
    <Styled.Container position={position} className={className}>
      {children}
    </Styled.Container>
  );
};

export default SidebarContainer;
