import { useQueryClient } from 'react-query';

import * as Styled from './index.styles';

interface NetworkErrorProps {
  className?: string;
  errorBoundaryReset: () => void;
}

const NetworkError = ({ className, errorBoundaryReset }: NetworkErrorProps) => {
  const queryClient = useQueryClient();

  const refetch = () => {
    queryClient.refetchQueries();
    errorBoundaryReset();
  };

  return (
    <Styled.Container className={className}>
      <Styled.Icon>🤯</Styled.Icon>
      네트워크 통신 도중 문제가 발생했습니다.
      <br />
      인터넷 연결 상태를 확인해주세요.
      <Styled.Refetch onClick={refetch}>다시 시도하기</Styled.Refetch>
    </Styled.Container>
  );
};

export default NetworkError;
