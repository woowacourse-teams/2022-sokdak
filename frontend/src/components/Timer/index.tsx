import { useEffect, useState } from 'react';

import * as Styled from './index.styles';

interface TimerProps {
  className?: string;
  startMinute: number;
  callback: () => void;
}

const Timer = ({ className, startMinute, callback }: TimerProps) => {
  const [totalSecond, setTotalSecond] = useState(startMinute * 60);
  const [minute, setMinute] = useState(startMinute);
  const [second, setSecond] = useState(0);

  useEffect(() => {
    const countdown = setInterval(() => {
      setTotalSecond(state => state - 1);
      setMinute(Math.floor(totalSecond / 60));
      setSecond(totalSecond % 60);
    }, 1000);

    if (totalSecond < 0) {
      clearInterval(countdown);
      callback();
    }

    return () => clearInterval(countdown);
  });

  return (
    <Styled.Container className={className}>
      {minute} : {second.toString().padStart(2, '0')}
    </Styled.Container>
  );
};

export default Timer;
