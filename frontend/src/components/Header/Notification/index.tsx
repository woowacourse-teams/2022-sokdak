import { useEffect, useRef, useState } from 'react';

import * as Styled from './index.styles';

const Notification = () => {
  const timer = useRef<NodeJS.Timer>();
  const [state, setState] = useState(false);
  useEffect(() => {
    timer.current = setInterval(() => {
      setState(prev => !prev);
    }, 30000);

    return () => {
      clearInterval(timer.current);
    };
  }, []);
  return (
    <Styled.NotificationContainer>
      {state && <Styled.AlarmPointer />}
      <Styled.NotificationIcon />
    </Styled.NotificationContainer>
  );
};

export default Notification;
