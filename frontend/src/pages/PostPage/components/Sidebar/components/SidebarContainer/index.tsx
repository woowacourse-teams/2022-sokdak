import { HTMLAttributes, PropsWithChildren, useRef } from 'react';

import * as Styled from './index.styles';

import useAnimateContainer from './useAnimateContainer';

const SidebarContainer = ({ children, className }: PropsWithChildren<HTMLAttributes<HTMLDivElement>>) => {
  const sideBarContainerRef = useRef<HTMLDivElement>(null);
  const sideBarRef = useRef<HTMLDivElement>(null);

  const { movementDegree } = useAnimateContainer({ parentContainer: sideBarContainerRef, childContainer: sideBarRef });

  return (
    <Styled.SidebarContainer ref={sideBarContainerRef}>
      <Styled.Container position={movementDegree} className={className} ref={sideBarRef}>
        {children}
      </Styled.Container>
    </Styled.SidebarContainer>
  );
};

export default SidebarContainer;
