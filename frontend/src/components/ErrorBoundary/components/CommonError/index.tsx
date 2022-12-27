import { AxiosError } from 'axios';

import * as Styled from './index.styles';

import NetworkError from '../NetworkError';
import ServerError from '../ServerError';

interface CommonError {
  error: Error | AxiosError | null;
  errorBoundaryReset: () => void;
}

const CommonError = ({ error, errorBoundaryReset }: CommonError) => {
  if (error && error instanceof AxiosError && error.response && error.response.status === 500) {
    return <ServerError serverMessage={error.response.data.message} errorBoundaryReset={errorBoundaryReset} />;
  }

  if (error && !navigator.onLine) {
    return <NetworkError errorBoundaryReset={errorBoundaryReset} />;
  }

  return (
    <Styled.Container>
      <Styled.Icon>🤯</Styled.Icon>
      <br />
      처리되지 않은 예외가 발생했습니다.
      <br />
      현상이 지속될 경우, 문의 바랍니다.
      <Styled.Information>({error?.message})</Styled.Information>
    </Styled.Container>
  );
};

export default CommonError;
