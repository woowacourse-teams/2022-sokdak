import { useState } from 'react';

const useInput = () => {
  const [value, setValue] = useState('');
  const [error, setError] = useState('');

  return { value, setValue, error, setError };
};

export { useInput };
