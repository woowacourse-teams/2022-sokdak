import { useEffect } from 'react';

interface UseModalCloseProps {
  closeModal: React.DispatchWithoutAction;
}

const useModalHistory = ({ closeModal }: UseModalCloseProps) => {
  const popModalHistory = () => {
    history.pushState(null, '', location.pathname);
    closeModal();
    history.go(-2);
  };

  useEffect(() => {
    history.pushState(null, '', location.pathname);
    window.addEventListener('popstate', popModalHistory);

    return () => {
      window.removeEventListener('popstate', popModalHistory);
    };
  }, []);
};

export default useModalHistory;
