import { useContext } from 'react';

import SnackbarContext from '@/context/Snackbar';

const useSnackbar = () => {
  const { isVisible, message, showSnackbar } = useContext(SnackbarContext);

  return { isVisible, message, showSnackbar };
};

export default useSnackbar;
