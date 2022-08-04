import { useEffect } from 'react';

interface UseModalHistoryProps {
  closeModal: React.DispatchWithoutAction;
}

const useModalHistory = ({ closeModal }: UseModalHistoryProps) => {
  const popModalHistory = () => closeModal();

  useEffect(() => {
    history.pushState(null, '');
    window.addEventListener('popstate', popModalHistory);

    return () => window.removeEventListener('popstate', popModalHistory);
  }, []);
};

export default useModalHistory;
