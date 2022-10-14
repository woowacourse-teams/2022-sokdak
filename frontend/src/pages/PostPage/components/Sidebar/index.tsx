import { HTMLAttributes, useEffect, useState } from 'react';

import useThrottle from '@/hooks/useThrottle';

import * as Styled from './index.styles';

interface SidebarProps extends HTMLAttributes<HTMLDivElement> {
  title: string;
  items: Item[];
  domain?: 'internal' | 'external';
}

interface Item {
  name: string;
  url: string;
}

const Sidebar = ({ title, items, className, domain = 'internal' }: SidebarProps) => {
  const [position, setPosition] = useState(document.documentElement.scrollTop);
  const moveSidebar = useThrottle(() => setPosition(document.documentElement.scrollTop), 50);

  useEffect(() => {
    window.addEventListener('scroll', moveSidebar);

    return () => window.removeEventListener('scroll', moveSidebar);
  }, []);

  return (
    <Styled.Container position={position} className={className}>
      <Styled.Title>{title}</Styled.Title>
      <Styled.Items>
        {items.map(({ name, url }) => (
          <Styled.Item key={name}>
            {domain === 'internal' ? (
              <Styled.InternalLink to={url}>{name}</Styled.InternalLink>
            ) : (
              <Styled.ExternalLink href={url} target="_blank">
                {name}
              </Styled.ExternalLink>
            )}
          </Styled.Item>
        ))}
      </Styled.Items>
    </Styled.Container>
  );
};

export default Sidebar;
