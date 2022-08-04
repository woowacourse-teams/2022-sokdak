import { useEffect, useState } from 'react';

import * as Styled from './index.styles';

interface TimerProps {
  className?: string;
  startMinute: number;
  callback: () => void;
}

const Timer = ({ className, startMinute, callback }: TimerProps) => {
  const [totalSecond, setTotalSecond] = useState(startMinute * 60);

  useEffect(() => {
    const countdown = setInterval(() => setTotalSecond(second => second - 1), 1000);

    if (totalSecond <= 0) {
      clearInterval(countdown);
      callback();
    }

    return () => clearInterval(countdown);
  });

  return (
    <Styled.Container className={className}>
      {Math.floor(totalSecond / 60)} : {(totalSecond % 60).toString().padStart(2, '0')}
    </Styled.Container>
  );
};

export default Timer;
