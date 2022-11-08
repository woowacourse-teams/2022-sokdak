import { useQueryClient } from 'react-query';

import * as Styled from './index.styles';

interface ServerErrorProps {
  className?: string;
  serverMessage: string;
  errorBoundaryReset: () => void;
}

const ServerError = ({ className, serverMessage, errorBoundaryReset }: ServerErrorProps) => {
  const queryClient = useQueryClient();

  const refetch = () => {
    queryClient.refetchQueries();
    errorBoundaryReset();
  };

  return (
    <Styled.Container className={className}>
      <Styled.Icon>🤯 500</Styled.Icon>
      서버에 알 수 없는 문제가 발생했습니다.
      <br />
      다시 시도하거나, 현상이 지속될 경우 문의 바랍니다.
      <br />
      <Styled.Information>({serverMessage})</Styled.Information>
      <Styled.Refetch onClick={refetch}>다시 시도하기</Styled.Refetch>
    </Styled.Container>
  );
};

export default ServerError;
