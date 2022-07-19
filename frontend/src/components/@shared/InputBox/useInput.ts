import { useState } from 'react';

const useInput = () => {
  const [value, setValue] = useState('');
  const [error, setError] = useState('');
  const [isAnimationActive, setIsAnimationActive] = useState(false);

  return { value, setValue, error, setError, isAnimationActive, setIsAnimationActive };
};

export { useInput };
